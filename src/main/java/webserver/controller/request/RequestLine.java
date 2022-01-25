package webserver.controller.request;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import util.HttpRequestUtils;
import webserver.common.HttpMethod;

public class RequestLine {
    private HttpMethod method;
    private String URL;
    private String path;
    private String http;
    private Map<String, String> queryString = Maps.newHashMap();

    public static RequestLine from(String RequestLine) {
        if (RequestLine == null) {
            return null;
        }
        RequestLine requestLine = new RequestLine();

        String[] lineTokens = RequestLine.split(" ");
        requestLine.method = HttpMethod.findMethod(lineTokens[0]);
        requestLine.URL = lineTokens[1];
        requestLine.http = lineTokens[2];

        String[] urlTokens = lineTokens[1].split("\\?");
        requestLine.path = urlTokens[0];

        if (urlTokens.length > 1) {
            requestLine.queryString.putAll(HttpRequestUtils.parseQueryString(urlTokens[1]));
        }
        return requestLine;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQueryStringParams(String key) {
        return queryString.get(key);
    }

    @Override
    public String toString() {
        return "RequestLine{" +
                "method='" + method + '\'' +
                ", URL='" + URL + '\'' +
                ", http='" + http + '\'' +
                '}';
    }
}
