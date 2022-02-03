package com.kakao.http.header;

public class SetCookieHeader extends AbstractHttpHeader {
    private static final String KEY = "Set-Cookie";

    private final String name;
    private final String value;
    private final String path;

    public SetCookieHeader(String name, String value, String path) {
        this.name = name;
        this.value = value;
        this.path = path;
    }

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public String value() {
        return String.format("%s=%s; Path=%s", this.name, this.value, this.path);
    }
}
