package com.sjz.lcsjz;

import com.sjz.lcsjz.core.service.schedule.ItemPriceScheduler;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SchedulerQueryMarketDataTest {

    @Resource
    private ItemPriceScheduler priceScheduler;


    @Test
    public void testQueryMarketData() {
        priceScheduler.execute();
    }
}
