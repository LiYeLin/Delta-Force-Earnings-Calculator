package com.sjz.lcsjz.core.service.impl;

import com.sjz.lcsjz.common.dal.entity.Items;
import com.sjz.lcsjz.common.dal.mapper.ItemsMapper;
import com.sjz.lcsjz.core.service.IItemsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 物品信息表，存储物品的静态属性 服务实现类
 * </p>
 *
 * @author enhe
 * @since 2025-07-29
 */
@Service
public class ItemsServiceImpl extends ServiceImpl<ItemsMapper, Items> implements IItemsService {

}
