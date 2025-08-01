package com.sjz.lcsjz.core.service.market.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjz.lcsjz.common.dal.entity.Items;
import com.sjz.lcsjz.common.dal.entity.PriceTicks;
import com.sjz.lcsjz.common.dal.mapper.PriceTicksMapper;
import com.sjz.lcsjz.core.service.market.IItemsService;
import com.sjz.lcsjz.core.service.market.IPriceTicksService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 价格历史表，存储最精细的时间序列价格数据 服务实现类
 * </p>
 *
 * @author enhe
 * @since 2025-07-29
 */
@Service
public class PriceTicksServiceImpl extends ServiceImpl<PriceTicksMapper, PriceTicks> implements IPriceTicksService {
    private static final Logger log = LoggerFactory.getLogger(PriceTicksServiceImpl.class);

    @Resource
    private IItemsService itemsService;

    /**
     * 批量保存价格快照数据。
     * 整个方法由一个事务包裹，确保数据一致性。
     *
     * @param priceTicksList 待处理的价格数据列表
     */
    @Transactional(rollbackFor = Exception.class) // 关键点1：添加事务注解，保证操作的原子性
    public void savePriceTicksBatch(List<PriceTicks> priceTicksList) {
        if (CollectionUtils.isEmpty(priceTicksList)) {
            return;
        }
        // --- 步骤1：数据准备与预处理
        // 1.1 在内存中过滤掉价格小于等于0的无效数据
        List<PriceTicks> validPriceTicks = priceTicksList.stream()
                .filter(p -> p.getPrice() != null && p.getPrice().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(validPriceTicks)) {
            log.info("【价格快照】所有传入数据价格均小于等于0，无需处理。");
            return;
        }

        // 1.2 一次性查询所有相关物品信息，用于日志记录和后续查找
        // a. 提取所有有效的物品ID
        List<Long> itemIds = validPriceTicks.stream()
                .map(PriceTicks::getItemId)
                .distinct()
                .collect(Collectors.toList());
        // b. 一次性查出所有物品，并转为Map<itemId, Items>，便于快速查找
        Map<Long, Items> itemsMap = itemsService.listByIds(itemIds).stream()
                .collect(Collectors.toMap(Items::getId, item -> item));

        // 1.3 一次性查询所有可能已存在的价格记录
        // a. 构建一个复杂的查询条件: (itemId=A AND tickAt=T1) OR (itemId=B AND tickAt=T2) ...
        LambdaQueryWrapper<PriceTicks> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(PriceTicks::getItemId, itemIds); // 先用IN缩小范围，提高效率
        queryWrapper.and(wrapper -> {
            for (PriceTicks tick : validPriceTicks) {
                wrapper.or(i -> i.eq(PriceTicks::getItemId, tick.getItemId()).eq(PriceTicks::getTickAt, tick.getTickAt()));
            }
        });
        // b. 一次性查出所有已存在的记录，并转为Map<"itemId:tickAt", PriceTicks>，便于快速判断
        Map<String, PriceTicks> existingTicksMap = this.list(queryWrapper).stream()
                .collect(Collectors.toMap(p -> p.getItemId() + ":" + p.getTickAt(), p -> p));


        // --- 步骤2：内存中进行逻辑判断与分组 ---

        List<PriceTicks> insertList = new ArrayList<>();
        List<PriceTicks> updateList = new ArrayList<>();

        for (PriceTicks incomingTick : validPriceTicks) {
            String key = incomingTick.getItemId() + ":" + incomingTick.getTickAt();
            Items item = itemsMap.get(incomingTick.getItemId());
            String itemName = (item != null) ? item.getItemName() : "未知物品"; // 处理物品不存在的情况

            PriceTicks existTick = existingTicksMap.get(key);

            if (existTick != null && !existTick.getPrice().equals(incomingTick.getPrice())) { //  价格不同，需要更新
                BigDecimal oldPrice = existTick.getPrice();
                existTick.setPrice(incomingTick.getPrice()); // 直接修改从数据库查出来的对象
                updateList.add(existTick);
                log.info("【价格快照】物品'{}' 时间点{} 待更新, 旧价格{}, 新价格{}", itemName, incomingTick.getTickAt(), oldPrice, incomingTick.getPrice());
            } else { // 如果记录不存在，需要插入
                insertList.add(incomingTick);
            }
        }
        // --- 步骤3：批量执行数据库操作 (最多2次DB写入) ---
        if (!CollectionUtils.isEmpty(insertList)) {
            this.saveBatch(insertList);
            log.info("【价格快照】成功批量插入 {} 条新数据。", insertList.size());
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            this.updateBatchById(updateList);
            log.info("【价格快照】成功批量更新 {} 条数据。", updateList.size());
        }
    }
}
