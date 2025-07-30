package com.sjz.lcsjz.common.integration.kkrb.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjz.lcsjz.common.integration.kkrb.KkrbClient;
import com.sjz.lcsjz.common.integration.model.ItemPriceFlow;
import com.sjz.lcsjz.common.integration.model.KkrbResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class KkrbClientImpl implements KkrbClient {
    private static final Logger log = LoggerFactory.getLogger(KkrbClientImpl.class);
    final String pageUrl = "https://www.kkrb725.com/?viewpage=view%2Fcollection%2Fammo_package";

    @Resource
    private RestClient kkrbRestClient;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    /**
     * 获取物品价格流水
     * @return 包含物品价格流水的KkrbResp对象
     */
    public KkrbResp<List<ItemPriceFlow>> getItemPriceFlow() {
        String version = getVersion();
        // 准备 application/x-www-form-urlencoded 格式的 body
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("version", version);

        String coreData = kkrbRestClient.post()
                .uri("/getAmmoPackageData")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .headers(headers -> {
                    headers.set("accept", "*/*");
                    headers.set("origin", "https://www.kkrb725.com");
                    headers.set("referer", pageUrl);
                    headers.set("x-requested-with", "XMLHttpRequest");
                    headers.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");
                })
                .body(formData)
                .retrieve()
                .body(String.class);

        try {
            return objectMapper.readValue(coreData, new TypeReference<KkrbResp<List<ItemPriceFlow>>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取版本号
     *
     * @return 版本号
     */
    public String getVersion() {
        ResponseEntity<String> responseEntity = kkrbRestClient.post()
                .uri("/getMenu")
                .contentType(MediaType.TEXT_PLAIN) // Sets Content-Type header
                .headers(headers -> {
                    // Add all specific headers for this request
                    headers.set("referer", "https://www.kkrb725.com/?viewpage=view%2Fmarket%2Fammo_price_pl");
                    headers.set("sec-ch-ua", "\"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"138\", \"Google Chrome\";v=\"138\"");
                })
                .body("") // Sending an empty body, RestClient will set Content-Length: 0
                .retrieve()
                .toEntity(String.class);
        String body = responseEntity.getBody();
        Map<String, Object> menuData = null;
        try {
            menuData = objectMapper.readValue(body, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("从menu中处理字段失败", e);
        }
        // 从解析后的Map中获取 built_ver
        if (menuData != null) {
            return (String) menuData.get("built_ver");
        }
        return null;
    }
}
