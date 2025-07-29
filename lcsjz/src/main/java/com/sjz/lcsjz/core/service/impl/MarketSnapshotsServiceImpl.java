package com.sjz.lcsjz.core.service.impl;

import com.sjz.lcsjz.common.dal.entity.MarketSnapshots;
import com.sjz.lcsjz.common.dal.mapper.MarketSnapshotsMapper;
import com.sjz.lcsjz.core.service.IMarketSnapshotsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 市场快照表，存储每个采集时间点的聚合分析数据 服务实现类
 * </p>
 *
 * @author enhe
 * @since 2025-07-29
 */
@Service
public class MarketSnapshotsServiceImpl extends ServiceImpl<MarketSnapshotsMapper, MarketSnapshots> implements IMarketSnapshotsService {

}
