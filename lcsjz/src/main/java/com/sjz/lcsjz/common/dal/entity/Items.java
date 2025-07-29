package com.sjz.lcsjz.common.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 物品信息表，存储物品的静态属性
 * </p>
 *
 * @author enhe
 * @since 2025-07-29
 */
@Getter
@Setter
@ToString
@ApiModel(value = "Items对象", description = "物品信息表，存储物品的静态属性")
public class Items implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 物品名称，唯一
     */
    @ApiModelProperty("物品名称，唯一")
    private String itemName;

    /**
     * 物品等级
     */
    @ApiModelProperty("物品等级")
    private Integer grade;

    /**
     * 物品图片链接
     */
    @ApiModelProperty("物品图片链接")
    private String picUrl;

    /**
     * 记录创建时间
     */
    @ApiModelProperty("记录创建时间")
    private LocalDateTime gmtCreate;

    /**
     * 记录最后修改时间
     */
    @ApiModelProperty("记录最后修改时间")
    private LocalDateTime gmtModified;
}
