package com.my.was.http.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestUri {

    private String path;
    private Map<String, String> params = new HashMap<>();

    public HttpRequestUri(String requestUri) {
        initialize(requestUri);
    }

    public void initialize(String requestUri) {
        if (isContainQueryString(requestUri)) {
            String[] seperatedPathAndParams = requestUri.split("\\?");
            path = seperatedPathAndParams[0];
            initializeParams(seperatedPathAndParams[1]);
            return;
        }

        path = requestUri;
    }

    private boolean isContainQueryString(String requestUri) {
        return requestUri.contains("?");
    }

    private void initializeParams(String params) {
        for (String param : params.split("&")) {
            String[] keyAndValue = param.split("=");
            this.params.put(URLDecoder.decode(keyAndValue[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(keyAndValue[1], StandardCharsets.UTF_8));
        }
    }

    public String getPath() {
        return path;
    }

    public String getParam(String paramName) {
        return params.get(paramName);
    }
}
