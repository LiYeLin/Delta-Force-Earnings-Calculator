package com.sjz.lcsjz.core.service.schedule;

import com.sjz.lcsjz.core.service.advice.AdviceService;
import com.sjz.lcsjz.core.service.market.IItemsService;
import com.sjz.lcsjz.core.service.model.Signal;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AnalyzeItemsScheduler {
    @Resource
    private AdviceService adviceService;
    @Resource
    private IItemsService itemsService;

    @Scheduled(cron = "0 15 * * * ?")
    public void analyzeItems() {
        log.info("analyzeItems start");
        adviceService.analyzeItems(itemsService.list(), List.of(Signal.BUY));
        log.info("analyzeItems end");
    }

}
