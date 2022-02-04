package com.kakao.http.request;

import com.kakao.http.common.HttpCookie;
import com.kakao.http.header.HttpHeader;
import com.kakao.util.HttpRequestUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HttpCookieStorage {
    private final Map<String, HttpCookie> nameMap;

    public HttpCookieStorage() {
        this.nameMap = Collections.emptyMap();
    }

    public HttpCookieStorage(HttpHeader cookieHeader) {
        this.nameMap = HttpRequestUtils.parseCookies(cookieHeader.value())
                .entrySet().stream()
                .map(entry -> new HttpCookie(entry.getKey(), entry.getValue()))
                .collect(Collectors.toMap(HttpCookie::name, Function.identity()));
    }

    public Optional<HttpCookie> findByName(String name) {
        return Optional.ofNullable(this.nameMap.get(name));
    }
}
