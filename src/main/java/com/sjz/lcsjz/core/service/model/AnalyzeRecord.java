package com.sjz.lcsjz.core.service.model;

import com.sjz.lcsjz.common.dal.entity.Items;

import java.time.LocalDateTime;

public record AnalyzeRecord(
        Items item,
        Signal signal,
        IndicatorContext context,
        LocalDateTime analyzeTime
) {
}
