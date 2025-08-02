package com.sjz.lcsjz.common.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjz.lcsjz.common.dal.entity.Items;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 物品信息表，存储物品的静态属性 Mapper 接口
 * </p>
 *
 * @author enhe
 * @since 2025-07-29
 */
@Mapper
public interface ItemsMapper extends BaseMapper<Items> {

}
