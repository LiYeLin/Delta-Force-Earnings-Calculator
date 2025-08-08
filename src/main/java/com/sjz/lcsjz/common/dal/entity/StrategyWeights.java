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
 * <p>
 * 
 * </p>
 *
 * @author enhe
 * @since 2025-08-07
 */
@Getter
@Setter
@ToString
@TableName("strategy_weights")
@ApiModel(value = "StrategyWeights对象", description = "")
public class StrategyWeights implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String strategyName;

    private String indicatorKey;

    private Integer weight;

    private String description;

    private Boolean isActive;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
