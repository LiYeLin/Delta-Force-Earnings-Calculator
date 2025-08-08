package com.sjz.lcsjz.core.service.model;


import java.math.BigDecimal;

// 使用Record定义一个不可变的建议对象
public record PersonalizedAdvice(
        Long userId,
        Long itemId,
        AdviceType adviceType,
        String title,
        String reason,
        BigDecimal currentPrice,
        BigDecimal costPrice,
        Priority priority
) {
    public enum AdviceType {
        STOP_LOSS_SELL,      // 止损卖出
        TARGET_PRICE_SELL,   // 目标价卖出
        STRATEGIC_SELL,      // 策略性止盈
        HOLD_PROFITABLE,     // 持有 (盈利中)
        HOLD_LOSING,         // 持有 (亏损中)
        HOLD_WATCH           // 持有 (需警惕)
    }

    public enum Priority {
        HIGH,   // 紧急，需立即通知
        MEDIUM, // 普通，可稍后通知
        LOW     // 无需主动通知，供查询
    }
}