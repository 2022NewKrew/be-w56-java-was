package com.kakao.http.request;

import com.kakao.util.HttpRequestUtils;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class Url {
    private final String url;
    private final String path;
    private final Map<String, String> queryMap;

    public Url(String url) {
        this.url = url;
        String[] urlTokens = this.url.split("\\?");
        queryMap = parseQueryMap(urlTokens);
        this.path = urlTokens[0];
    }

    private Map<String, String> parseQueryMap(String[] urlTokens) {
        switch (urlTokens.length) {
            case 1:
                return Collections.emptyMap();
            case 2:
                return Collections.unmodifiableMap(HttpRequestUtils.parseQueryString(urlTokens[1]));
            default:
                throw new IllegalArgumentException("The url should have up to 2 tokens.");
        }
    }
}
