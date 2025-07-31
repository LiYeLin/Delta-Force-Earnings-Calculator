package com.sjz.lcsjz.core.service.market;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sjz.lcsjz.common.dal.entity.Items;

/**
 * <p>
 * 物品信息表，存储物品的静态属性 服务类
 * </p>
 *
 * @author enhe
 * @since 2025-07-29
 */
public interface IItemsService extends IService<Items> {

    /**
     * 保存商品信息
     *
     * @param item 商品对象
     * @return 返回保存后的商品ID
     */
    Long saveItem(Items item);
}
