package com.sjz.lcsjz.common.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndicatorCalculatorUtil {
    // 为所有除法运算定义一个统一的精度和舍入模式，避免无限小数和保证结果一致性
    private static final int DEFAULT_SCALE = 8;
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final MathContext MC = new MathContext(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);

    /**
     * 计算指数移动平均线 (EMA)
     *
     * @param prices 价格列表
     * @param period 周期 (例如 12, 26)
     * @return EMA值列表，与价格列表等长，无法计算的部分为null
     */
    public static List<BigDecimal> calculateEMA(List<BigDecimal> prices, int period) {
        if (prices == null || prices.size() < period) {
            return Collections.emptyList();
        }

        List<BigDecimal> emaValues = new ArrayList<>(Collections.nCopies(prices.size(), null));
        BigDecimal periodDecimal = BigDecimal.valueOf(period);

        // 1. 计算初始EMA值 (使用前N个周期的简单移动平均SMA)
        BigDecimal initialSma = prices.subList(0, period).stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(periodDecimal, MC);
        emaValues.set(period - 1, initialSma);

        // 2. 计算平滑系数 alpha = 2 / (period + 1)
        BigDecimal alpha = BigDecimal.valueOf(2).divide(periodDecimal.add(BigDecimal.ONE), MC);

        // 3. 迭代计算后续的EMA值
        // 公式: EMA_current = (Price_current * alpha) + (EMA_previous * (1 - alpha))
        for (int i = period; i < prices.size(); i++) {
            BigDecimal currentPrice = prices.get(i);
            BigDecimal previousEma = emaValues.get(i - 1);

            BigDecimal currentEma = currentPrice.multiply(alpha)
                    .add(previousEma.multiply(BigDecimal.ONE.subtract(alpha)));

            emaValues.set(i, currentEma);
        }

        return emaValues;
    }

    /**
     * 计算相对强弱指数 (RSI)
     *
     * @param prices 价格列表
     * @param period 周期 (通常为 14)
     * @return RSI值列表，与价格列表等长，无法计算的部分为null
     */
    public static List<BigDecimal> calculateRSI(List<BigDecimal> prices, int period) {
        if (prices == null || prices.size() <= period) {
            return Collections.emptyList();
        }

        List<BigDecimal> rsiValues = new ArrayList<>(Collections.nCopies(prices.size(), null));
        BigDecimal periodDecimal = BigDecimal.valueOf(period);

        // 1. 计算价格变化量，并分离出Gains和Losses
        List<BigDecimal> gains = new ArrayList<>();
        List<BigDecimal> losses = new ArrayList<>();
        for (int i = 1; i < prices.size(); i++) {
            BigDecimal delta = prices.get(i).subtract(prices.get(i - 1));
            if (delta.compareTo(BigDecimal.ZERO) > 0) {
                gains.add(delta);
                losses.add(BigDecimal.ZERO);
            } else {
                gains.add(BigDecimal.ZERO);
                losses.add(delta.abs());
            }
        }

        // 2. 计算初始的平均增益和平均损失
        BigDecimal initialAvgGain = gains.subList(0, period).stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(periodDecimal, MC);
        BigDecimal initialAvgLoss = losses.subList(0, period).stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(periodDecimal, MC);

        // 3. 迭代计算平滑后的平均值和RSI
        BigDecimal avgGain = initialAvgGain;
        BigDecimal avgLoss = initialAvgLoss;

        for (int i = period; i < gains.size() + 1; i++) {
            // 计算RS和RSI
            if (avgLoss.compareTo(BigDecimal.ZERO) == 0) {
                rsiValues.set(i, BigDecimal.valueOf(100)); // 如果平均损失为0，RSI为100
            } else {
                BigDecimal rs = avgGain.divide(avgLoss, MC);
                BigDecimal rsi = BigDecimal.valueOf(100).subtract(
                        BigDecimal.valueOf(100).divide(BigDecimal.ONE.add(rs), MC)
                );
                rsiValues.set(i, rsi);
            }

            // 为下一次循环更新平滑平均值
            // 公式: AvgGain_current = ((AvgGain_previous * (period - 1)) + Gain_current) / period
            if (i < gains.size()) {
                avgGain = avgGain.multiply(periodDecimal.subtract(BigDecimal.ONE))
                        .add(gains.get(i))
                        .divide(periodDecimal, MC);
                avgLoss = avgLoss.multiply(periodDecimal.subtract(BigDecimal.ONE))
                        .add(losses.get(i))
                        .divide(periodDecimal, MC);
            }
        }
        return rsiValues;
    }
}
