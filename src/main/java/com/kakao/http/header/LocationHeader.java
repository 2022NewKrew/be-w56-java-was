package com.kakao.http.header;

public class LocationHeader extends AbstractHttpHeader {
    private static final String KEY = "Location";

    private final String url;

    public LocationHeader(String url) {
        this.url = url;
    }

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public String value() {
        return url;
    }
}
