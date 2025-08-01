package com.sjz.lcsjz.common.configurations;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个用于 OkHttp 4.x 的、自定义的、线程安全的内存 CookieJar 实现。
 */
public final class MyCookieJar implements CookieJar {

    private final ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

    /**
     * 当收到服务器响应时，OkHttp 会调用此方法。
     * @param url 请求的URL
     * @param cookies 服务器在 Set-Cookie 头中返回的 Cookie 列表
     */
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.put(url.host(), cookies);
    }

    /**
     * 当准备发起新请求时，OkHttp 会调用此方法。
     * @param url 即将请求的URL
     * @return 需要附加到请求头中的 Cookie 列表
     */
    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url.host());
        return cookies != null ? cookies : new ArrayList<>();
    }
}