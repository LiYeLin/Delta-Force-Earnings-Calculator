package com.sjz.lcsjz.core.service.model;


import org.springframework.lang.Nullable;

import java.math.BigDecimal;

public record IndicatorContext(
        // 持仓信息
        @Nullable
        HoldingPositionDetail holdingPositionDetail,
        // 核心价格
        BigDecimal currentPrice,
        // 基准指标
        BigDecimal p20Price,
        BigDecimal p80Price,
        // 情绪指标
        BigDecimal rsi,

        // 波动性指标 布林带 下轨
        BigDecimal bollingerLowerBand,
        // 布林带 上轨
        BigDecimal bollingerUpperBand,

        // 趋势指标
        boolean isGoldenCross // ema12 > ema26
) {}