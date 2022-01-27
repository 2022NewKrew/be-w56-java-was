package webserver.controller.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import util.HttpRequestUtils;
import webserver.common.HttpMethod;

public class RequestLine {
    private HttpMethod method;
    private String URL;
    private String path;
    private String http;
    private Map<String, String> queryString = Maps.newHashMap();

    private static final Logger log = LoggerFactory.getLogger(RequestLine.class);

    public static RequestLine from(String RequestLine) {
        if (RequestLine == null) {
            return null;
        }
        log.info("Request Line : {}", RequestLine);

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
