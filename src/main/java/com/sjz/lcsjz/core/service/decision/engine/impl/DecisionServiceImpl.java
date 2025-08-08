package com.sjz.lcsjz.core.service.decision.engine.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sjz.lcsjz.common.dal.entity.Items;
import com.sjz.lcsjz.common.dal.mapper.ItemsMapper;
import com.sjz.lcsjz.common.util.IndicatorCalculatorUtil;
import com.sjz.lcsjz.core.service.decision.engine.DecisionService;
import com.sjz.lcsjz.core.service.model.*;
import com.sjz.lcsjz.core.service.signa.SignalGeneratorService;
import com.sjz.lcsjz.core.service.view.MarketViewService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DecisionServiceImpl implements DecisionService {
    // 新建guava缓存 key: itemName value: AnalyzeRecord
    public static Cache<String, AnalyzeRecord> ANALYZE_CACHE = CacheBuilder.newBuilder()
            .maximumSize(2000)  // 设置最大条目数为 1000
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    @Resource
    private ItemsMapper itemMapper;
    @Resource
    private MarketViewService marketViewQueryService;
    @Resource
    private SignalGeneratorService signalGeneratorService;

    /**
     * 对物品进行全面的技术分析，判断其当前的市场状态，完全不考虑任何用户。
     *
     * @param item 物品信息
     * @return 分析结果 包含信号（买入or持有 ps 此时与用户无关 所以没有卖出信号）
     */
    @Override
    public AnalyzeRecord analyzeSingleItem(Items item) {
        Long itemId = item.getId();
        log.debug("Analyzing item: {}", item.getItemName());

        // 1. 从查询服务获取预计算好的数据 (拉取30天确保有足够数据计算指标)
        List<BollingerBandDTO> bbandsData = marketViewQueryService.queryBollingerBands(itemId, 30);
        DailyPercentileDTO percentileData = marketViewQueryService.queryTodayPercentile(itemId);

        // 数据完整性校验
        if (bbandsData.isEmpty() || percentileData == null || bbandsData.size() < 26) { // 26是EMA最长周期
            log.warn("物品 [id={}, name={}] 的数据不足，跳过分析。", itemId, item.getItemName());
            throw new IllegalArgumentException("数据不足以分析");
        }

        // 2. 在Java内存中计算EMA和RSI
        List<BigDecimal> prices = bbandsData.stream().map(BollingerBandDTO::price).collect(Collectors.toList());
        List<BigDecimal> ema12 = IndicatorCalculatorUtil.calculateEMA(prices, 12);
        List<BigDecimal> ema26 = IndicatorCalculatorUtil.calculateEMA(prices, 26);
        List<BigDecimal> rsi14 = IndicatorCalculatorUtil.calculateRSI(prices, 14);

        // 3. 构建最新时间点的指标上下文 (IndicatorContext)
        int lastIndex = prices.size() - 1;
        BollingerBandDTO lastBbandData = bbandsData.get(lastIndex);

        // // 获取持仓成本价，用于卖出决策 (如果存在)
        // BigDecimal buyPrice = portfolioService.getHolding(CURRENT_USER_ID, itemId)
        //         .map(Portfolio::getAvgBuyPrice)
        //         .orElse(null);

        IndicatorContext context = new IndicatorContext(
                null,
                lastBbandData.price(),
                percentileData.p20Price(),
                percentileData.p80Price(), // 假设p30Price暂时用p20代替
                rsi14.get(lastIndex),
                lastBbandData.bbandsLower(),
                lastBbandData.bbandsUpper(),
                ema12.get(lastIndex).compareTo(ema26.get(lastIndex)) > 0 // isGoldenCross
        );

        // 4. 调用决策服务生成信号
        Signal finalSignal = signalGeneratorService.generateSignal(context);

        // 5. 封装结果并写入guava缓存
        AnalyzeRecord response = new AnalyzeRecord(item, finalSignal, context, LocalDateTime.now());
        ANALYZE_CACHE.put(item.getItemName(), response);
        // jackson json打印response
        log.info("Analyze result: {}", response);
        return response;
    }
}
