package com.sjz.lcsjz.common.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 价格历史表，存储最精细的时间序列价格数据
 * </p>
 *
 * @author enhe
 * @since 2025-07-29
 */
@Getter
@Setter
@ToString
@TableName("price_ticks")
@ApiModel(value = "PriceTicks对象", description = "价格历史表，存储最精细的时间序列价格数据")
public class PriceTicks implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 外键，关联到items表
     */
    @ApiModelProperty("外键，关联到items表")
    private Long itemId;

    /**
     * 价格的精确时间点
     */
    @ApiModelProperty("价格的精确时间点")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime tickAt;

    /**
     * 该时间点的价格
     */
    @ApiModelProperty("该时间点的价格")
    private BigDecimal price;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;
}
