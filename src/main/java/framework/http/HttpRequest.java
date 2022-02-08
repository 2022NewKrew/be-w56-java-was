package framework.http;

import framework.util.HttpRequestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {
    private Map<String, String> headers;
    private Map<String, String> queryParameter;
    private Map<String, String> body;
    private Map<String, String> cookies;
    private HttpMethod httpMethod;
    private String url;

    public HttpRequest(String reqLine, String reqHeader) {
        httpMethod = HttpRequestUtils.parseMethod(reqLine);

        requestParsingQueryParam(HttpRequestUtils.parseUrl(reqLine));
        requestParsingHeader(reqHeader);
        body = new HashMap<>();
        cookies = new HashMap<>();
        if (headers.containsKey("Cookie")) {
            requestParsingCookie(headers.get("Cookie"));
        }
    }

    private void requestParsingQueryParam(String url) {
        String[] parseUrl = url.split("[?]");

        this.url = parseUrl[HttpConst.URI_PATH];
        queryParameter = new HashMap<>();
        if (parseUrl.length > HttpConst.URI_QUERY_PARAM) {
            queryParameter = HttpRequestUtils.parseQueryString(parseUrl[HttpConst.URI_QUERY_PARAM]);
        }
    }

    private void requestParsingHeader(String reqHeader) {
        headers = Arrays.stream(reqHeader.split("[\r\n]"))
                .map(HttpRequestUtils::parseHeader)
                .collect(Collectors.toMap(
                        HttpRequestUtils.Pair::getKey,
                        HttpRequestUtils.Pair::getValue
                ));
    }

    private void requestParsingCookie(String cookies) {
        this.cookies = HttpRequestUtils.parseCookies(cookies);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public HttpMethod getMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public String getQueryParameter(String key) {
        return queryParameter.get(key);
    }

    public String getBody(String key) {
        return body.get(key);
    }

    public void setBody(String body) {
        this.body = HttpRequestUtils.parseQueryString(body);
    }


    public String getCookie(String key) {
        return cookies.get(key);
    }
}
