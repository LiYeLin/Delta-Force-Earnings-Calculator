package com.sjz.lcsjz.core.service.signa;

import com.sjz.lcsjz.core.service.model.IndicatorContext;
import com.sjz.lcsjz.core.service.model.Signal;

public interface SignalGeneratorService {
    /**
     * 生成信号的方法
     *
     * @param context 指标上下文对象
     * @return 生成的信号
     */
    Signal generateSignal(IndicatorContext context);
}
