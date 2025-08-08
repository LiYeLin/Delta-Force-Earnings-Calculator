package com.sjz.lcsjz.core.service.schedule;

import com.sjz.lcsjz.common.dal.entity.Items;
import com.sjz.lcsjz.common.dal.entity.PriceTicks;
import com.sjz.lcsjz.common.dal.mapper.ItemsMapper;
import com.sjz.lcsjz.common.integration.kkrb.KkrbClient;
import com.sjz.lcsjz.common.integration.model.ItemPriceFlow;
import com.sjz.lcsjz.common.integration.model.KkrbResp;
import com.sjz.lcsjz.core.service.market.IMarketSnapshotsService;
import com.sjz.lcsjz.core.service.market.IPriceTicksService;
import com.sjz.lcsjz.core.service.market.impl.ItemsServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class ItemPriceScheduler {
    private static final DateTimeFormatter yyyyMMddHH = DateTimeFormatter.ofPattern("yyyyMMddHH");

    @Resource
    private KkrbClient kkrbClient;
    @Autowired
    private ItemsServiceImpl itemsService;
    @Resource
    private ItemsMapper mapper;
    @Resource
    private IPriceTicksService priceTicksService;
    @Resource
    private IMarketSnapshotsService marketSnapshotsService;

    /**
     * 执行物品价格数据定时任务
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void execute() {
        log.info("===【物品价格数据Scheduler】【启动】===");
        KkrbResp<List<ItemPriceFlow>> itemPriceFlowResp = null;
        try {
            // 调用kkrbClient的接口获取物品价格流列表
            itemPriceFlowResp = kkrbClient.getItemPriceFlow();
        } catch (Exception e) {
            log.error("【物品价格数据Scheduler】获取物品价格失败", e);
            return;
        }
        // 从返回的物品价格流列表中distinct去重，并转换为列表
        List<ItemPriceFlow> PriceFlowOfItems = itemPriceFlowResp.getData().stream().distinct().toList();
        for (ItemPriceFlow flow : PriceFlowOfItems) {
            // 1.保存物品
            Items item = convertToItem(flow);
            // 保存物品实体类到数据库
            Long itemId = Optional.ofNullable(itemsService.saveItem(item)).orElseThrow(() -> new RuntimeException("保存物品失败"));
            // 2.保存价格快照
            // 将价格曲线中的每个元素转换为PriceTicks对象，并设置相应的属性
            Map<String, BigDecimal> priceCurve = flow.getPriceCurve();
            if (priceCurve == null || priceCurve.isEmpty()) {
                log.error("【物品价格数据Scheduler】物品{} 价格曲线为空", item.getItemName());
                continue;
            }
            List<PriceTicks> priceTicksList = priceCurve.entrySet().stream().map(entry -> {
                PriceTicks priceTicks = new PriceTicks();
                priceTicks.setItemId(itemId);
                // 将时间字符串解析为LocalDateTime类型
                priceTicks.setTickAt(LocalDateTime.parse(entry.getKey(), yyyyMMddHH));
                // 将价格字符串解析为BigDecimal类型
                BigDecimal price = entry.getValue();
                priceTicks.setPrice(price);
                return priceTicks;
            }).toList();
            // 保存价格快照到数据库
            priceTicksService.savePriceTicksBatch(priceTicksList);
            log.info("【物品价格数据Scheduler】物品{} 价格快照保存完成，共{}条", item.getItemName(), priceTicksList.size());
        }
        log.info("===【物品价格数据Scheduler】【结束】===");
    }

    /**
     * 将ItemPriceFlow对象转换为Items对象
     *
     * @param flow ItemPriceFlow对象
     * @return Items对象
     */
    private Items convertToItem(ItemPriceFlow flow) {
        // 创建一个新的Items对象
        Items item = new Items();
        // 设置Items对象的属性值为ItemPriceFlow对象的属性值
        item.setItemName(flow.getItemName());
        item.setPicUrl(flow.getPic());
        item.setGrade(flow.getGrade());
        // 返回转换后的Items对象
        return item;
    }


}
