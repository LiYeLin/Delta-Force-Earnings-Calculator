package com.sjz.lcsjz.core.service.indicator.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjz.lcsjz.common.dal.entity.StrategyWeights;
import com.sjz.lcsjz.common.dal.mapper.StrategyWeightsMapper;
import com.sjz.lcsjz.core.service.indicator.IStrategyWeightsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author enhe
 * @since 2025-08-07
 */
@Service
public class StrategyWeightsServiceImpl extends ServiceImpl<StrategyWeightsMapper, StrategyWeights> implements IStrategyWeightsService {
    @Resource
    private StrategyWeightsMapper strategyWeightMapper;

    @Override
    public Map<String, Integer> getStrategyWeights(String strategyName) {
        List<StrategyWeights> weights = strategyWeightMapper.selectList(null); // 查询所有
        // 按strategy_name分组
        Map<String, Map<String, Integer>> newCache = weights.stream()
                .filter(StrategyWeights::getIsActive)
                .collect(Collectors.groupingBy(
                        StrategyWeights::getStrategyName,
                        Collectors.toMap(StrategyWeights::getIndicatorKey, StrategyWeights::getWeight)
                ));
        return newCache.get(strategyName);

    }
}
