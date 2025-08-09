package com.sjz.lcsjz.core.service.view.Impl;

import com.sjz.lcsjz.common.dal.mapper.MaintenanceMapper;
import com.sjz.lcsjz.common.dal.mapper.MarketViewMapper;
import com.sjz.lcsjz.core.service.model.BollingerBandDTO;
import com.sjz.lcsjz.core.service.model.DailyPercentileDTO;
import com.sjz.lcsjz.core.service.view.MarketViewService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MarketViewServiceImpl implements MarketViewService {
    @Resource
    private MaintenanceMapper maintenanceMapper;
    @Resource
    private MarketViewMapper marketViewMapper;

    @Override
    public void refreshDailyPercentilesView() {
        log.info("Starting to refresh materialized view: mv_daily_percentiles");
        try {
            // 调用Mapper方法执行刷新
            maintenanceMapper.refreshMvDailyPercentiles();
            log.info("Successfully refreshed materialized view: mv_daily_percentiles");
        } catch (Exception e) {
            log.error("Failed to refresh materialized view: mv_daily_percentiles", e);
        }
    }

    @Override
    public void refreshBollingerBandsView() {
        log.info("Starting to refresh materialized view: mv_bollinger_bands");
        try {
            // 调用Mapper方法执行刷新
            maintenanceMapper.refreshMvBollingerBands();
            log.info("Successfully refreshed materialized view: mv_bollinger_bands");
        } catch (Exception e) {
            log.error("Failed to refresh materialized view: mv_bollinger_bands", e);
        }
    }

    @Override
    public List<BollingerBandDTO> queryBollingerBands(Long itemId, int days) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        return marketViewMapper.getBollingerBandsSince(itemId, startTime);
    }

    @Override
    public DailyPercentileDTO queryTodayPercentile(Long itemId) {
        return marketViewMapper.getTodayPercentile(itemId);
    }
}
