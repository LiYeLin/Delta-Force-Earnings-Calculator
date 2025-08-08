package com.sjz.lcsjz.core.service.model;

import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 持仓明细
 * @param buyPrice
 * @param buyTime
 * @param size
 */
public record HoldingPositionDetail(BigDecimal buyPrice, // 仅在判断卖出时需要
                                    @Nullable
                                    LocalDateTime buyTime, // 仅在判断卖出时需要
                                    Integer size) {
}