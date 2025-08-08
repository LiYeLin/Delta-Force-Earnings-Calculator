package com.sjz.lcsjz.core.service.decision.engine;

import com.sjz.lcsjz.common.dal.entity.Items;
import com.sjz.lcsjz.core.service.model.AnalyzeRecord;

public interface DecisionService {
    /**
     * 对物品进行全面的技术分析，判断其当前的市场状态，完全不考虑任何用户。
     *
     * @param item 物品信息
     * @return 分析结果 包含信号（买入or持有 ps 此时与用户无关 所以没有卖出信号）
     */
    AnalyzeRecord analyzeSingleItem(Items item);
}
