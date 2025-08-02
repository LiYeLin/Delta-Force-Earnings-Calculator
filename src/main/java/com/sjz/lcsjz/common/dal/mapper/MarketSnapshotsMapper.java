package com.sjz.lcsjz.common.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjz.lcsjz.common.dal.entity.MarketSnapshots;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 市场快照表，存储每个采集时间点的聚合分析数据 Mapper 接口
 * </p>
 *
 * @author enhe
 * @since 2025-07-29
 */
@Mapper
public interface MarketSnapshotsMapper extends BaseMapper<MarketSnapshots> {

}
