package com.sjz.lcsjz.common.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 市场快照表，存储每个采集时间点的聚合分析数据
 * </p>
 *
 * @author enhe
 * @since 2025-07-29
 */
@Getter
@Setter
@ToString
@TableName("market_snapshots")
@ApiModel(value = "MarketSnapshots对象", description = "市场快照表，存储每个采集时间点的聚合分析数据")
public class MarketSnapshots implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 外键，关联到items表
     */
    @ApiModelProperty("外键，关联到items表")
    private Long itemId;

    /**
     * 数据抓取的时间点
     */
    @ApiModelProperty("数据抓取的时间点")
    private LocalDateTime snapshotAt;

    private BigDecimal currentPrice;

    private BigDecimal yesterdayLowestPrice;

    private String yesterdayLowestDatetime;

    private BigDecimal yesterdayHighestPrice;

    private BigDecimal yesterdayHighestPriceAfterTax;

    private String yesterdayHighestDatetime;

    private BigDecimal yesterdayPriceOffset;

    private BigDecimal yesterdayPriceOffsetWithoutTax;

    private BigDecimal lastWeekLowestPrice;

    private BigDecimal lastWeekLowestPriceAfterTax;

    private LocalDateTime lastWeekLowestDatetime;

    private BigDecimal lastWeekHighestPrice;

    private BigDecimal lastWeekHighestPriceAfterTax;

    private LocalDateTime lastWeekHighestDatetime;

    private BigDecimal lastWeekPriceOffset;

    private BigDecimal thisWeekHighestPrice;

    private BigDecimal thisWeekHighestPriceAfterTax;

    private LocalDateTime thisWeekHighestDatetime;

    private BigDecimal thisWeekPriceOffset;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
