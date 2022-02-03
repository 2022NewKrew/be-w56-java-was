package com.kakao.http.header;

import java.util.Map;
import java.util.stream.Collectors;

public class SetCookieHeader extends AbstractHttpHeader {
    private static final String KEY = "Set-Cookie";

    private final Map<String, String> cookieMap;

    public SetCookieHeader(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public String value() {
        return cookieMap.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(";"));
    }
}
