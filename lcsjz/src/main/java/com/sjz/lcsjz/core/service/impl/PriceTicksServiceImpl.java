package com.sjz.lcsjz.core.service.impl;

import com.sjz.lcsjz.common.dal.entity.PriceTicks;
import com.sjz.lcsjz.common.dal.mapper.PriceTicksMapper;
import com.sjz.lcsjz.core.service.IPriceTicksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 价格历史表，存储最精细的时间序列价格数据 服务实现类
 * </p>
 *
 * @author enhe
 * @since 2025-07-29
 */
@Service
public class PriceTicksServiceImpl extends ServiceImpl<PriceTicksMapper, PriceTicks> implements IPriceTicksService {

}
