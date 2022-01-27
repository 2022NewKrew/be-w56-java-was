package http.util;

import http.HttpBody;
import http.HttpHeaders;
import http.request.RequestParams;
import http.request.StartLine;

import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.Map;

import static http.util.HttpRequestUtils.parseQueryString;

public class HttpRequestParamsParser {

    public static RequestParams parse(StartLine startLine, HttpHeaders httpHeaders, HttpBody httpBody) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.putAll(parseQueryString(startLine.getUrl().getQueryString()));

        if(httpHeaders.getHeader(HttpHeaders.CONTENT_TYPE).orElseGet(()->"").equals("application/x-www-form-urlencoded")) {
            paramMap.putAll(parseQueryString(httpBody.toString(StandardCharsets.UTF_8)));
        }

        return RequestParams.of(paramMap);
    }
}
