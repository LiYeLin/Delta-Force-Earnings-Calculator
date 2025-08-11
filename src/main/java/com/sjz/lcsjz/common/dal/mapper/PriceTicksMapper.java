package com.sjz.lcsjz.common.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjz.lcsjz.common.dal.entity.PriceTicks;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 价格历史表，存储最精细的时间序列价格数据 Mapper 接口
 * </p>
 *
 * @author enhe
 * @since 2025-07-29
 */
@Mapper
public interface PriceTicksMapper extends BaseMapper<PriceTicks> {
    /**
     * 批量保存价格数据 by uk
     *
     * @param priceTicksList
     */
    void upsertBatch(@Param("list") List<PriceTicks> priceTicksList);

}
