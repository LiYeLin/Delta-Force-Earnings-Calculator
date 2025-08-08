package com.sjz.lcsjz.common.configurations;


import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.CookieManager;
import java.net.CookiePolicy;

@Configuration
public class MyRestClientConfig {

    @Bean(name = "kkrbRestClient")
    public RestClient restClient() {
        // 1. 创建一个 Cookie 管理器，它会自动处理 Set-Cookie 和 Cookie 头
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        // 2. 创建一个带有 CookieJar 的 OkHttpClient

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new MyCookieJar())
                .build();
        // 3. 将 OkHttpClient 设置为 RestClient 的底层工厂
        ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);

        return RestClient.builder().requestFactory(requestFactory)
                .baseUrl("https://www.kkrb725.com")
                .defaultHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36")
                .defaultHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                .defaultHeader("accept", "*/*")
                .defaultHeader("cache-control", "no-cache")
                .defaultHeader("dnt", "1")
                .defaultHeader("origin", "https://www.kkrb725.com")
                .defaultHeader("pragma", "no-cache")
                .defaultHeader("priority", "u=1, i")
                .defaultHeader("sec-ch-ua-mobile", "?0")
                .defaultHeader("sec-ch-ua-platform", "\"macOS\"")
                .defaultHeader("sec-fetch-dest", "empty")
                .defaultHeader("sec-fetch-mode", "cors")
                .defaultHeader("sec-fetch-site", "same-origin")
                .defaultHeader("x-requested-with", "XMLHttpRequest")
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}