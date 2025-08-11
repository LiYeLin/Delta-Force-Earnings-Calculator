package com.sjz.lcsjz.core.service.advice;

import com.google.common.base.Preconditions;
import com.sjz.lcsjz.common.dal.entity.Items;
import com.sjz.lcsjz.core.service.decision.engine.DecisionService;
import com.sjz.lcsjz.core.service.model.AnalyzeRecord;
import com.sjz.lcsjz.core.service.model.Signal;
import com.sjz.lcsjz.core.service.notify.NotificationService;
import com.sjz.lcsjz.core.service.view.MarketViewService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j(topic = "DecisionLogger")
@Service
public class AdviceService {

    @Resource
    private DecisionService decisionService;
    @Resource
    private MarketViewService marketViewService;
    @Resource
    private NotificationService notificationService;

    /**
     * 分析物品价格
     */
    public void analyzeItems(List<Items> scope, List<Signal> passSignals) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(scope), "物品列表不能为空");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(passSignals), "信号列表不能为空");
        // 打印日志，表示开始分析物品价格
        log.info("【投资信号分析】【start】分析物品价格");
        long l = System.currentTimeMillis();
        // 1. 刷新市场视图
        beforeAnalyze();
        // 遍历物品列表，逐个分析物品价格
        List<AnalyzeRecord> usefulSignalList = scope.stream().map(item -> {
                    log.info("【投资信号分析】开始分析{}物品", item.getItemName());
                    AnalyzeRecord analyzeRecord = null;
                    try {
                        // 调用决策服务分析单个物品价格
                        analyzeRecord = decisionService.analyzeSingleItem(item);
                    } catch (Exception e) {
                        // 打印错误日志，表示分析物品价格失败
                        log.error("【投资信号分析】【fail】分析物品价格失败，itemName: {}", item.getItemName(), e);
                        return null;
                    }
                    // 打印日志，表示分析物品价格完成
                    log.info("【投资信号分析】【success】分析物品价格完成，itemName: {},结果：{}", item.getItemName(), analyzeRecord);
                    return analyzeRecord;
                })
                .filter(Objects::nonNull)
                .filter(analyzeRecord -> passSignals.contains(analyzeRecord.signal()))
                .toList();

        log.info("【投资信号分析】【end】分析物品价格完成,处理时间:{}ms", System.currentTimeMillis() - l);
        afterAnalyze(usefulSignalList);
    }

    private void beforeAnalyze() {
        marketViewService.refreshDailyPercentilesView();
        marketViewService.refreshBollingerBandsView();
        log.info("【投资信号分析】刷新市场视图完成");
    }

    private void afterAnalyze(List<AnalyzeRecord> usefulSignalList) {
        // 通知我
        notificationService.sendSignals(usefulSignalList);

    }
}
