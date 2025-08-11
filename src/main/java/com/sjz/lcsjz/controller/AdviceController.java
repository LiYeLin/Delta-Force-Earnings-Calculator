package com.sjz.lcsjz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.base.Preconditions;
import com.sjz.lcsjz.common.dal.entity.Items;
import com.sjz.lcsjz.core.service.advice.AdviceService;
import com.sjz.lcsjz.core.service.market.IItemsService;
import com.sjz.lcsjz.core.service.model.Signal;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/advice")
public class AdviceController {
    @Resource
    private AdviceService adviceService;
    @Resource
    private IItemsService itemsService;

    @GetMapping
    public void list(String keyword) {
        Preconditions.checkArgument(keyword != null, "关键字不能为空");
        List<Items> list = itemsService.list(new LambdaQueryWrapper<Items>().like(Items::getItemName, keyword));
        if (!list.isEmpty())
            adviceService.analyzeItems(list, List.of(Signal.values()));
    }

}
