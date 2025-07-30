package com.sjz.lcsjz.common.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

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
    private int grade;
    /**
     * 当前价格（含税）
     */
    @JsonProperty("currentPrice")
    private double currentPrice;
    /**
     * 本周最高价（含税）
     */
    private double thisWeekHighestPrice;
    /**
     * 本周最高价（税后）
     */
    private double thisWeekHighestPriceAfterTax;
    /**
     * 本周最高价出现时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    private LocalDateTime thisWeekHighestDatetime;
    /**
     * 本周价格涨跌幅（相比上周）
     */
    private double thisWeekPriceOffset;
    /**
     * 上周最高价（含税）
     */
    private double lastWeekHighestPrice;
    /**
     * 上周最高价（税后）
     */
    private double lastWeekHighestPriceAfterTax;
    /**
     * 上周最高价出现时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    private LocalDateTime lastWeekHighestDatetime;
    /**
     * 上周最低价（含税）
     */
    private double lastWeekLowestPrice;
    /**
     * 上周最低价（税后）
     */
    private double lastWeekLowestPriceAfterTax;
    /**
     * 上周最低价出现时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    private LocalDateTime lastWeekLowestDatetime;
    /**
     * 上周价格涨跌幅
     */
    private double lastWeekPriceOffset;
    /**
     * 昨日最高价（含税）
     */
    private double yesterdayHighestPrice;
    /**
     * 昨日最高价（税后）
     */
    private double yesterdayHighestPriceAfterTax;
    /**
     * 昨日最高价出现时间（自然语言描述，如"凌晨0点"）
     */
    private String yesterdayHighestDatetime;
    /**
     * 昨日最低价（含税）
     */
    private double yesterdayLowestPrice;
    /**
     * 昨日最低价（税后）
     */
    private double yesterdayLowestPriceAfterTax;
    /**
     * 昨日最低价出现时间（自然语言描述，如"晚上8点"）
     */
    private String yesterdayLowestDatetime;
    /**
     * 昨日价格涨跌幅（含税）
     */
    private double yesterdayPriceOffset;
    /**
     * 昨日价格涨跌幅（不含税）
     */
    private double yesterdayPriceOffsetWithoutTax;
    /**
     * 价格曲线数据（Key为时间戳格式yyyyMMddHH，Value为价格）
     */
    private Map<String, String> priceCurve;
}
