package com.sjz.lcsjz.core.service.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Bollinger带数据传输对象
 */
public record BollingerBandDTO(
        /*
          商品id
         */
        Long itemId,
        /*
          时间戳
         */
        LocalDateTime tickAt,
        /*
          价格
         */
        BigDecimal price,
        /*
          中轨
         */
        BigDecimal bbandsMiddle,
        /*
          上轨
         */
        BigDecimal bbandsUpper,
        /*
          下轨
         */
        BigDecimal bbandsLower
) {
}
