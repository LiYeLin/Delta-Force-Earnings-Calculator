package com.sjz.lcsjz.common.integration.kkrb;

import com.sjz.lcsjz.common.integration.model.ItemPriceFlow;
import com.sjz.lcsjz.common.integration.model.KkrbResp;

import java.util.List;

public interface KkrbClient {
    KkrbResp<List<ItemPriceFlow>> getItemPriceFlow();
} 