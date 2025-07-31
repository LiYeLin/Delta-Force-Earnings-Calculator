package com.sjz.lcsjz.common.integration.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 商品价格趋势
 */
@Data
public class ItemPriceFlow {
    /**
     * 商品名称
     */
    private String itemName;
    /**
     * 商品图片URL
     */
    private String pic;
    /**
     * 商品等级
     */
    private Integer grade;
    /**
     * 当前价格（含税）
     */
    @JsonProperty("currectPrice")
    private BigDecimal currentPrice;
    /**
     * 本周最高价（含税）
     */
    private BigDecimal thisWeekHighestPrice;
    /**
     * 本周最高价（税后）
     */
    private BigDecimal thisWeekHighestPriceAfterTax;
    /**
     * 本周最高价出现时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thisWeekHighestDatetime;
    /**
     * 本周价格涨跌幅（相比上周）
     */
    private BigDecimal thisWeekPriceOffset;
    /**
     * 上周最高价（含税）
     */
    private BigDecimal lastWeekHighestPrice;
    /**
     * 上周最高价（税后）
     */
    private BigDecimal lastWeekHighestPriceAfterTax;
    /**
     * 上周最高价出现时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastWeekHighestDatetime;
    /**
     * 上周最低价（含税）
     */
    private BigDecimal lastWeekLowestPrice;
    /**
     * 上周最低价（税后）
     */
    private BigDecimal lastWeekLowestPriceAfterTax;
    /**
     * 上周最低价出现时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastWeekLowestDatetime;
    /**
     * 上周价格涨跌幅
     */
    private BigDecimal lastWeekPriceOffset;
    /**
     * 昨日最高价（含税）
     */
    private BigDecimal yesterdayHighestPrice;
    /**
     * 昨日最高价（税后）
     */
    private BigDecimal yesterdayHighestPriceAfterTax;
    /**
     * 昨日最高价出现时间（自然语言描述，如"凌晨0点"）
     */
    private String yesterdayHighestDatetime;
    /**
     * 昨日最低价（含税）
     */
    private BigDecimal yesterdayLowestPrice;
    /**
     * 昨日最低价（税后）
     */
    private BigDecimal yesterdayLowestPriceAfterTax;
    /**
     * 昨日最低价出现时间（自然语言描述，如"晚上8点"）
     */
    private String yesterdayLowestDatetime;
    /**
     * 昨日价格涨跌幅（含税）
     */
    private BigDecimal yesterdayPriceOffset;
    /**
     * 昨日价格涨跌幅（不含税）
     */
    private BigDecimal yesterdayPriceOffsetWithoutTax;
    /**
     * 价格曲线数据（Key为时间戳格式yyyyMMddHH，Value为价格）
     */
    private Map<String, BigDecimal> priceCurve;
}
