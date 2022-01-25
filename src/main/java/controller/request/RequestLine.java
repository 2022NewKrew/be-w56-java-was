package controller.request;

import com.google.common.collect.Maps;
import util.HttpRequestUtils;

import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-25 025
 * Time: 오후 6:48
 */
public class RequestLine {
    private String URL;
    private String path;
    private String contentType;
    private String http;
    private Map<String, String> queryString = Maps.newHashMap();

    public static RequestLine from(String RequestLine) {
        if (RequestLine == null) {
            return null;
        }

        RequestLine requestLine = new RequestLine();

        String[] lineTokens = RequestLine.split(" ");
        requestLine.URL = lineTokens[1];
        requestLine.http = lineTokens[2];

        String[] urlTokens = lineTokens[1].split("\\?");
        requestLine.path = urlTokens[0];

        if (urlTokens.length > 1) {
            requestLine.queryString.putAll(HttpRequestUtils.parseQueryString(urlTokens[1]));
        }

        String[] urlTokens2 = lineTokens[1].split("\\.");
        requestLine.contentType = urlTokens2[urlTokens2.length - 1];

        return requestLine;
    }

    public String getPath() {
        return path;
    }

    public String getContentType() {
        return contentType;
    }

    public String getQueryStringParams(String key) {
        return queryString.get(key);
    }
}
