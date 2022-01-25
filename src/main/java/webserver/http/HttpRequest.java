package webserver.http;

import util.HttpRequestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {
    private Map<String, String> headers;
    private Map<String, String> queryParameter;
    private HttpMethod httpMethod;
    private String url;

    public HttpRequest(String reqLine, String reqHeader){
        httpMethod = HttpRequestUtils.parseMethod(reqLine);

        requestParsingQueryParam(HttpRequestUtils.parseUrl(reqLine));
        requestParsingHeader(reqHeader);
    }

    private void requestParsingQueryParam(String url){
        String[] parseUrl = url.split("[?]");

        this.url = parseUrl[0];
        queryParameter = new HashMap<>();
        if(parseUrl.length > 1){
            queryParameter = HttpRequestUtils.parseQueryString(parseUrl[1]);
        }
    }

    private void requestParsingHeader(String reqHeader){
        headers = Arrays.stream(reqHeader.split("\r|\n"))
                .map(HttpRequestUtils::parseHeader)
                .collect(Collectors.toMap(
                        HttpRequestUtils.Pair::getKey,
                        HttpRequestUtils.Pair::getValue
                ));
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpMethod getMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getQueryParameter() {
        return queryParameter;
    }
}
