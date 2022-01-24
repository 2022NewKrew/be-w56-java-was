package com.kakao.webserver;

public abstract class AbstractHttpHeader implements HttpHeader {
    @Override
    public String toString() {
        return String.format("%s: %s\r\n", key(), value());
    }
}
