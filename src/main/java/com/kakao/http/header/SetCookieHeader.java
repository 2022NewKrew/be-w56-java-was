package com.kakao.http.header;

import com.kakao.http.common.HttpCookie;

public class SetCookieHeader extends AbstractHttpHeader {
    private static final String KEY = "Set-Cookie";

    private final HttpCookie cookie;

    public SetCookieHeader(HttpCookie cookie) {
        this.cookie = cookie;
    }

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public String value() {
        return cookie.toString();
    }
}
