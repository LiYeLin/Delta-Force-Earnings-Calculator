package com.sjz.lcsjz.core.service.market.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjz.lcsjz.common.dal.entity.Items;
import com.sjz.lcsjz.common.dal.mapper.ItemsMapper;
import com.sjz.lcsjz.core.service.market.IItemsService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ItemsServiceImpl extends ServiceImpl<ItemsMapper, Items> implements IItemsService {

    /**
     * 保存物品信息
     *
     * @param item 物品对象
     * @return 保存成功后的物品ID
     */
    public Long saveItem(Items item) {
        // 通过LambdaQueryChainWrapper查询已经存在的同名物品
        Items one = lambdaQuery().select(Items::getId).eq(Items::getItemName, item.getItemName()).one();
        if (one != null) {
            if (!item.equals(one)) {
                item.setId(one.getId());
                updateById(item);
                log.info("【物品服务】物品{} 已存在，字段有差异，更新完成", item.getItemName());
            }
            // 如果已经存在同名物品，则返回已存在物品的ID
            return one.getId();
        }
        // 如果不存在同名物品，则保存当前物品信息
        boolean save = save(item);
        if (!save) {
            // 如果保存失败，则抛出运行时异常
            throw new RuntimeException("物品保存失败");
        }
        // 保存成功后，打印日志并返回物品ID
        log.info("【物品服务】物品{} 保存完成", item.getItemName());
        return item.getId();
    }
}
