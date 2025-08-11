package com.sjz.lcsjz;

import com.sjz.lcsjz.core.service.schedule.AnalyzeItemsScheduler;
import com.sjz.lcsjz.core.service.schedule.ItemPriceScheduler;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback
public class SchedulerQueryMarketDataTest {
    @Resource
    private AnalyzeItemsScheduler analyzeItemsScheduler;
    @Resource
    private ItemPriceScheduler itemPriceScheduler;

    @Test
    public void test() {
        // itemPriceScheduler.execute();
    }
}
