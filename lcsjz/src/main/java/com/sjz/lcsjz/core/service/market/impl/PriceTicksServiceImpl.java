package com.sjz.lcsjz.core.service.market.impl;

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
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

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
     * 批量保存价格数据
     *
     * @param priceTicksList 价格数据列表
     * @return 保存是否成功
     */
    public Boolean savePriceTicksBatch(List<PriceTicks> priceTicksList) {
        // 如果价格数据列表为空，则直接返回true
        if (CollectionUtils.isEmpty(priceTicksList)) {
            return true;
        }
        // 遍历价格数据列表
        for (PriceTicks priceTicks : priceTicksList) {
            // 根据物品ID获取物品信息
            Items item = itemsService.getById(priceTicks.getItemId());
            // 如果价格小于等于0，则跳过保存
            if (priceTicks.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                log.info("【物品价格数据Scheduler】物品{} 时间点{} 价格小于=0，跳过保存", item.getItemName(), priceTicks.getTickAt());
                continue;
            }
            // 根据物品ID和时间点查询是否已存在该价格数据
            PriceTicks exist = lambdaQuery().eq(PriceTicks::getItemId, item.getId()).eq(PriceTicks::getTickAt, priceTicks.getTickAt()).one();
            // 如果已存在该价格数据
            if (exist != null) {
                // 如果价格不同，则更新价格数据
                if (!exist.getPrice().equals(priceTicks.getPrice())) {
                    BigDecimal oldPrice = exist.getPrice();
                    exist.setPrice(priceTicks.getPrice());
                    updateById(exist);
                    log.info("物品{} 时间点{} 价格快照数据被重刷，已更新,旧价格{},新价格{}", item.getItemName(), priceTicks.getTickAt(), oldPrice, priceTicks.getPrice());
                    continue;
                }
                // 如果价格相同，则跳过保存
                log.info("【物品价格数据Scheduler】物品{} 时间点{} 价格快照已存在，跳过保存", item.getItemName(), priceTicks.getTickAt());
                continue;
            }
            // 如果不存在该价格数据，则保存该价格数据
            return save(priceTicks);
        }
        return true;
    }
}
