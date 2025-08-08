package com.sjz.lcsjz.core.service.view;

import com.sjz.lcsjz.core.service.model.BollingerBandDTO;
import com.sjz.lcsjz.core.service.model.DailyPercentileDTO;

import java.util.List;

/**
 * 市场视图服务接口
 */
public interface MarketViewService {

    /**
     * 刷新每日百分位数视图
     */
    void refreshDailyPercentilesView();


    /**
     * 刷新布林带视图
     */
    void refreshBollingerBandsView();

    /**
     * 查询指定物品在指定天数内的布林带数据
     *
     * @param itemId 物品ID
     * @param days   天数
     * @return 布林带数据列表
     */
    List<BollingerBandDTO> queryBollingerBands(Long itemId, int days);

    /**
     * 查询指定物品今日的百分位数数据
     *
     * @param itemId 物品ID
     * @return 百分位数数据
     */
    DailyPercentileDTO queryTodayPercentile(Long itemId);
}
