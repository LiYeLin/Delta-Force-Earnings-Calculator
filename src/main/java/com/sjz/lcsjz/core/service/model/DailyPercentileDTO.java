package com.sjz.lcsjz.core.service.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyPercentileDTO(
        // 商品id
        Long itemId,
        // 日期
        LocalDate forDate,
        // p20价格
        BigDecimal p20Price,
        // p80价格
        BigDecimal p80Price
) {}
