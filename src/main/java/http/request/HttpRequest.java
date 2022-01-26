package http.request;

import http.HttpHeaders;
import http.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static http.util.HttpRequestUtils.parseQueryString;

public class HttpRequest {

    private final StartLine startLine;

    private final HttpHeaders headers;

    private final RequestBody body;

    private final RequestParams params;

    private HttpRequest(StartLine startLine, HttpHeaders headers, RequestBody body, RequestParams params) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
        this.params = params;
    }

    public static HttpRequest createHttpRequest(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StartLine startLine = StartLine.create(br);
        HttpHeaders header = HttpHeaders.create(br);

        Integer contentLength = Integer.valueOf(header.getHeader(HttpHeaders.CONTENT_LENGTH).orElse("0"));
        RequestBody body = RequestBody.create(br, contentLength, StandardCharsets.UTF_8);

        Map<String, Object> parameterMap = new HashMap<>();
        Map<String, String> queryMap = parseQueryString(startLine.getUrl().getQueryString());
        for(Map.Entry<String, String> query : queryMap.entrySet()) {
            parameterMap.put(query.getKey(), query.getValue());
        }

        if(header.getHeader(HttpHeaders.CONTENT_TYPE).orElseGet(()->"").equals("application/x-www-form-urlencoded")
        && startLine.getHttpMethod().equals(HttpMethod.POST)) {
            Map<String, String> queryMap2 = parseQueryString(body.toString(StandardCharsets.UTF_8));
            for(Map.Entry<String, String> query : queryMap2.entrySet()) {
                parameterMap.put(query.getKey(), query.getValue());
            }
        }

        return new HttpRequest(startLine, header, body, RequestParams.of(parameterMap));
    }

    public String getUrl() {
        return startLine.getUrl().getPath();
    }

    public HttpMethod getMethod() {
        return startLine.getHttpMethod();
    }

    public String getHttpVersion() {
        return startLine.getHttpVersion();
    }

    public RequestBody getBody() {
        return body;
    }

    public Object getParameter(String key) {
        return params.getValue(key);
    }
}
