package com.sjz.lcsjz.common.dal.dao; // <-- 确认这是您存放实体类的包！

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.Instant;

/**
 * 物品实体类
 * 使用Lombok的@Data注解自动生成getter/setter等方法
 * 使用MyBatis-Plus的注解来映射表和字段
 */
@Data
@TableName("market_data.items") // 指定表名，并包含schema
public class Item {

    @TableId(value = "id", type = IdType.AUTO) // 声明主键为id，并设置为自增
    private Long id;

    private String itemName;

    private Integer grade;

    private String picUrl;
    
    // MyBatis-Plus可以配置自动填充gmt_create和gmt_modified，这里先简单定义
    private Instant gmtCreate;

    private Instant gmtModified;
}