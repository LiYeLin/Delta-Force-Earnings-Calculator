package com.sjz.lcsjz.core.service.schedule;

import com.sjz.lcsjz.common.dal.entity.Items;
import com.sjz.lcsjz.core.service.decision.engine.DecisionService;
import com.sjz.lcsjz.core.service.market.IItemsService;
import com.sjz.lcsjz.core.service.model.AnalyzeRecord;
import com.sjz.lcsjz.core.service.model.Signal;
import com.sjz.lcsjz.core.service.notify.NotificationService;
import com.sjz.lcsjz.core.service.view.MarketViewService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AnalyzeItemsScheduler {

    /**
     * 决策服务
     */
    @Resource
    private DecisionService decisionService;
    /**
     * 物品服务
     */
    @Resource
    private IItemsService itemsService;

    @Resource
    private MarketViewService marketViewService;
    @Resource
    private NotificationService notificationService; // 注入通知服务

    /**
     * 分析物品价格
     */
    @Scheduled(cron = "0 15 * * * ?")
    public void analyzeItems() {
        // 打印日志，表示开始分析物品价格
        log.info("【物品价格分析】【start】分析物品价格");
        long l = System.currentTimeMillis();
        // 1. 刷新市场视图
        beforeAnalyze();
        // 2.获取所有物品列表
        List<Items> list = itemsService.list();
        // 遍历物品列表，逐个分析物品价格
        List<AnalyzeRecord> usefulSignalList = list.stream().map(item -> {
                    AnalyzeRecord analyzeRecord = null;
                    try {
                        // 调用决策服务分析单个物品价格
                        analyzeRecord = decisionService.analyzeSingleItem(item);
                    } catch (Exception e) {
                        // 打印错误日志，表示分析物品价格失败
                        log.error("【物品价格分析】【fail】分析物品价格失败，itemName: {}", item.getItemName(), e);
                    }
                    // 打印日志，表示分析物品价格完成
                    log.info("【物品价格分析】【success】分析物品价格完成，itemName: {},结果：{}", item.getItemName(), analyzeRecord);
                    return analyzeRecord;
                }).filter(analyzeRecord -> !Signal.HOLD.equals(analyzeRecord.signal()))
                .toList();

        log.info("【物品价格分析】【end】分析物品价格完成,处理时间:{}ms", System.currentTimeMillis() - l);
        afterAnalyze(usefulSignalList);
    }

    private void beforeAnalyze() {
        marketViewService.refreshDailyPercentilesView();
        marketViewService.refreshBollingerBandsView();
        log.info("【物品价格分析】刷新市场视图完成");
    }

    private void afterAnalyze(List<AnalyzeRecord> usefulSignalList) {
        //     通知我
        notificationService.sendSignals(usefulSignalList);

    }
}
