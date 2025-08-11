package com.sjz.lcsjz.common.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 用户持仓实体类
 * enhe
 * 2025-08-08
 */
@Getter
@Setter
@ToString
@TableName("user_holding")
@ApiModel(value = "UserHolding对象", description = "")
public class UserHolding implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long itemId;

    /**
     * 平均成本价
     */
    private Integer avgCostPrice;
    /**
     * 总数量
     */
    private Integer totalAmount;
    /**
     * 目标卖出价
     */
    private Integer targetSellPrice;
    /**
     * 止损价
     */
    private Integer stopLossPrice;
    /**
     * 状态
     */
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;
}
