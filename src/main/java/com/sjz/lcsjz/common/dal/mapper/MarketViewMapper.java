package com.sjz.lcsjz.common.dal.mapper;

import com.sjz.lcsjz.core.service.model.BollingerBandDTO;
import com.sjz.lcsjz.core.service.model.DailyPercentileDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

public interface MarketViewMapper {
    /**
     * 查询单个物品在指定时间段内的布林带数据
     * @param itemId 物品ID
     * @param startTime 查询的起始时间
     * @return 布林带数据列表
     */
    @Select("SELECT item_id, tick_at, price, bbands_middle, bbands_upper, bbands_lower " +
            "FROM market_data.mv_bollinger_bands " +
            "WHERE item_id = #{itemId} AND tick_at >= #{startTime} " +
            "ORDER BY tick_at ")
    List<BollingerBandDTO> getBollingerBandsSince(@Param("itemId") Long itemId, @Param("startTime") LocalDateTime startTime);

    /**
     * 查询单个物品当天的P20/P80价格
     * @param itemId 物品ID
     * @return 当日的百分位数据
     */
    @Select("SELECT item_id, for_date, p20_price, p80_price " +
            "FROM market_data.mv_daily_percentiles " +
            "WHERE item_id = #{itemId} AND for_date::date = CURRENT_DATE")
    DailyPercentileDTO getTodayPercentile(@Param("itemId") Long itemId);

}
