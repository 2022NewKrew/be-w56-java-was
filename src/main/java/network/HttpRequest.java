package network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> queryString;
    private final Map<String, String> headers;
    private final String body;

    public HttpRequest(Map<String, String> tokens, Map<String, String> headers, String body) {
        this.method = HttpMethod.valueOf(tokens.get("method"));
        this.path = tokens.get("path");
        this.queryString = HttpRequestUtils.parseQueryString(tokens.get("queryString"));
        this.headers = headers;
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getQueryString() {
        return queryString;
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        String accept = headers.get("Accept");
        return HttpRequestUtils.contentNegotation(accept);
    }

}
