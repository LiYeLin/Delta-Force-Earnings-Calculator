package com.sjz.lcsjz.common.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Integer avgCostPrice;

    private Integer totalAmount;

    private Integer targetSellPrice;

    private Integer stopLossPrice;

    private String status;
}
