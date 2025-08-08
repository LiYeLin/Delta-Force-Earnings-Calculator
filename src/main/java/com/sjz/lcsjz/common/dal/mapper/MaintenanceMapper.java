package com.sjz.lcsjz.common.dal.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 用于执行数据库维护相关操作的Mapper，如刷新物化视图。
 */
@Mapper
public interface MaintenanceMapper {

    /**
     * 无锁刷新布林带物化视图
     */
    @Update("REFRESH MATERIALIZED VIEW CONCURRENTLY market_data.mv_bollinger_bands;")
    void refreshMvBollingerBands();

    /**
     * 无锁刷新每日百分位物化视图
     */
    @Update("REFRESH MATERIALIZED VIEW CONCURRENTLY market_data.mv_daily_percentiles;")
    void refreshMvDailyPercentiles();

}