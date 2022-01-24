package com.kakao.webserver;

public class ContentLengthHeader extends AbstractHttpHeader {
    private static final String KEY = "Content-Length";

    private final String value;

    public ContentLengthHeader(long length) {
        this.value = String.valueOf(length);
    }

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public String value() {
        return this.value;
    }
}
