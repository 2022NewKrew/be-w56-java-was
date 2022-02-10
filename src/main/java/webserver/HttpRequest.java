package webserver;

import util.HttpRequestUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final HttpMethod method;
    private final String uri;
    private final String path;
    private final String version;
    private final Map<String, String> headers;
    private final Map<String, String> queryString;
    private Map<String, String> cookies;
    private Map<String, String> body;

    public HttpRequest(HttpMethod method, String uri, String version, Map<String, String> headers) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.headers = headers;
        this.cookies = new HashMap<>();

        if (uri.contains("?")) {
            String[] split = uri.split("\\?");
            this.path = split[0];
            this.queryString = HttpRequestUtils.parseQueryString(split[1]);
        } else {
            this.path = uri;
            this.queryString = new HashMap<>();
        }
        if (headers.containsKey("Cookie")){
            this.cookies = HttpRequestUtils.parseCookies(headers.get("Cookie"));
        }
    }

    public HttpRequest(HttpMethod method, String uri, String version, Map<String, String> headers, Map<String, String> body) {
        this(method, uri, version, headers);
        this.body = body;
    }

    public HttpMethod getMethod() {
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

    public Map<String, String> getBody() {
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

    public String toMessage() {
        String requestMessage = String.format("%s %s %s\r\n", method, uri, version);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestMessage += String.format("%s: %s\r\n", entry.getKey(), entry.getValue());
        }
        requestMessage += "\r\n";
        requestMessage += body;
        return requestMessage;
    }
}
