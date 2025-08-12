package com.sjz.lcsjz.core.service.signa.impl;

import com.sjz.lcsjz.core.service.indicator.IStrategyWeightsService;
import com.sjz.lcsjz.core.service.model.HoldingPositionDetail;
import com.sjz.lcsjz.core.service.model.IndicatorContext;
import com.sjz.lcsjz.core.service.model.Signal;
import com.sjz.lcsjz.core.service.signa.SignalGeneratorService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j(topic = "DecisionLogger")
public class SignalGeneratorServiceImpl implements SignalGeneratorService {

    /**
     * 策略权重服务
     */
    @Resource
    private IStrategyWeightsService strategyWeightsService;

    /**
     * 根据给定的指标上下文生成交易信号
     *
     * @param context 指标上下文
     * @return 交易信号
     */
    @Override
    public Signal generateSignal(IndicatorContext context) {
        int sellScore = generateSellScore(context);
        int buyScore = generateBuyScore(context);
        int sellThreshold = getThreshold("DEFAULT_SELL");
        int buyThreshold = getThreshold("DEFAULT_BUY");
        log.info("[信号生成], 物品：{},卖出分: {}, 卖出阈值: {}, 买入分: {}, 买入阈值: {}",
                context.item().getItemName(), sellScore, sellThreshold, buyScore, buyThreshold);
        // 首先判断卖出信号
        if (context.holdingPositionDetail() != null && sellScore >= sellThreshold) {
            return Signal.SELL;
        }
        // 再判断买入信号
        if (buyScore >= buyThreshold) {
            return Signal.BUY;
        }
        // 默认为持有
        return Signal.HOLD;
    }

    /**
     * 生成买入信号的分数
     *
     * @param ctx 指标上下文
     * @return 分数
     */
    private int generateBuyScore(IndicatorContext ctx) {
        Map<String, Integer> weights = strategyWeightsService.getStrategyWeights("DEFAULT_BUY");
        if (weights == null || weights.isEmpty())
            return 0;

        int score = 0;
        if (ctx.currentPrice().compareTo(ctx.p20Price()) <= 0)
            score += weights.getOrDefault("PRICE_LTE_P20", 0);
        if (ctx.rsi().compareTo(new BigDecimal("30")) <= 0)
            score += weights.getOrDefault("RSI_LTE_30", 0);
        else if (ctx.rsi().compareTo(new BigDecimal("40")) <= 0)
            score += weights.getOrDefault("RSI_LTE_40", 0);

        if (ctx.currentPrice().compareTo(ctx.bollingerLowerBand()) <= 0)
            score += weights.getOrDefault("PRICE_LTE_BOLLINGER_LOWER", 0);

        if (ctx.isGoldenCross())
            score += weights.getOrDefault("EMA_GOLDEN_CROSS", 0);

        return score;
    }

    /**
     * 生成卖出信号的分数
     *
     * @param ctx 指标上下文
     * @return 分数
     */
    private int generateSellScore(IndicatorContext ctx) {
        Map<String, Integer> weights = strategyWeightsService.getStrategyWeights("DEFAULT_SELL");
        if (weights == null || weights.isEmpty())
            return 0;
        BigDecimal buyPrice = Optional.ofNullable(ctx.holdingPositionDetail()).map(HoldingPositionDetail::buyPrice).orElse(BigDecimal.ZERO);
        // 核心前提：必须盈利
        BigDecimal breakEvenPrice = buyPrice.divide(new BigDecimal("0.85"), 2, RoundingMode.HALF_UP);
        if (ctx.currentPrice().compareTo(breakEvenPrice) <= 0) {
            return 0; // 未过保本线，不考虑卖出
        }

        int score = 0;
        if (ctx.currentPrice().compareTo(ctx.p80Price()) >= 0)
            score += weights.getOrDefault("PRICE_GTE_P80", 0);
        if (ctx.rsi().compareTo(new BigDecimal("70")) >= 0)
            score += weights.getOrDefault("RSI_GTE_70", 0);
        if (ctx.currentPrice().compareTo(ctx.bollingerUpperBand()) >= 0)
            score += weights.getOrDefault("PRICE_GTE_BOLLINGER_UPPER", 0);

        return score;
    }

    /**
     * 获取策略的阈值
     *
     * @param strategyName 策略名称
     * @return 阈值
     */
    private int getThreshold(String strategyName) {
        Map<String, Integer> weights = strategyWeightsService.getStrategyWeights(strategyName);
        return (weights != null) ? weights.getOrDefault("SCORE_THRESHOLD", 1000) : 1000; // 默认一个很高的阈值，防止出错
    }
}
