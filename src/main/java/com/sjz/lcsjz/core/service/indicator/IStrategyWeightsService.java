package com.sjz.lcsjz.core.service.indicator;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sjz.lcsjz.common.dal.entity.StrategyWeights;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author enhe
 * @since 2025-08-07
 */
public interface IStrategyWeightsService extends IService<StrategyWeights> {
    Map<String, Integer> getStrategyWeights(String strategyName);
}
