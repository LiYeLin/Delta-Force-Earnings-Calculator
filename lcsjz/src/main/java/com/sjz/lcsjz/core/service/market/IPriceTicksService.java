package com.sjz.lcsjz.core.service.market;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sjz.lcsjz.common.dal.entity.PriceTicks;

import java.util.List;

/**
 * <p>
 * 价格历史表，存储最精细的时间序列价格数据 服务类
 * </p>
 *
 * @author enhe
 * @since 2025-07-29
 */
public interface IPriceTicksService extends IService<PriceTicks> {
    /**
     * 批量保存价格数据
     *
     * @param priceTicksList 价格数据列表
     * @return 保存是否成功
     */
    Boolean savePriceTicksBatch(List<PriceTicks> priceTicksList);
}
