package webserver;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final String method; // TODO : enum 클래스로?!
    private final String uri;
    private final String path;
    private final String version;
    private final String body;
    private Map<String, String> headers;
    private Map<String, String> queryString;
    private Map<String, String> cookies;

    public HttpRequest(String method, String uri, String version, Map<String, String> headers, String body) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.path = uri.split("\\?")[0];
        this.body = body;
        this.headers = headers;
        this.queryString = new HashMap<>();
        this.cookies = new HashMap<>();

        if (uri.split("\\?").length >= 2) {
            this.queryString = HttpRequestUtils.parseQueryString(uri.split("\\?")[1]);
        }
        if (headers.containsKey("Cookie")){
            this.cookies = HttpRequestUtils.parseCookies(headers.get("Cookie"));
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getQueryString() {
        return queryString;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    @Override
    public String toString() {
        String responseMessage = String.format("%s %s %s \n", method, uri, version);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            responseMessage += String.format("%s: %s\n", entry.getKey(), entry.getValue());
        }
        return responseMessage;
    }
}
