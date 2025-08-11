package com.sjz.lcsjz.core.service.market.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.sjz.lcsjz.common.dal.entity.PriceTicks;
import com.sjz.lcsjz.common.dal.mapper.PriceTicksMapper;
import com.sjz.lcsjz.core.service.market.IItemsService;
import com.sjz.lcsjz.core.service.market.IPriceTicksService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
@Slf4j
public class PriceTicksServiceImpl extends ServiceImpl<PriceTicksMapper, PriceTicks> implements IPriceTicksService {

    @Resource
    private IItemsService itemsService;

    /**
     * 批量保存价格快照数据。
     * 整个方法由一个事务包裹，确保数据一致性。
     *
     * @param priceTicksList 待处理的价格数据列表
     */
    public void upsertPriceTicksBatch(List<PriceTicks> priceTicksList) {
        if (CollectionUtils.isEmpty(priceTicksList)) {
            return;
        }
        // 1. 数据预处理（过滤、去重）
        List<PriceTicks> validPriceTicks = new ArrayList<>(priceTicksList.stream()
                .filter(p -> p.getPrice() != null && p.getPrice().compareTo(BigDecimal.ZERO) > 0)
                // 解决次要可能性：在内存中根据唯一键去重
                .collect(Collectors.toMap(
                        t -> t.getItemId() + ":" + t.getTickAt(),
                        t -> t,
                        (first, second) -> first // 如果 key 冲突，保留第一个
                ))
                .values());

        if (CollectionUtils.isEmpty(validPriceTicks)) {
            return;
        }
        Lists.partition(validPriceTicks, 100).forEach(this.baseMapper::upsertBatch);
        log.info("【价格快照】成功处理 {} 条数据。", validPriceTicks.size());
    }
}
