package com.kakao.http.header;

public class BasicHeader extends AbstractHttpHeader {
    private final String key;
    private final String value;

    public BasicHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public String value() {
        return this.value;
    }
}
