package http;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import util.HttpRequestUtils;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> queries;
    private final Map<String, String> headers;
    private Map<String, String> params;

    private HttpRequest(HttpMethod method, String path, Map<String, String> queries, Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.queries = queries;
        this.headers = headers;
    }

    public static HttpRequest from(List<String> requestLines) {
        String[] tokens = HttpRequestUtils.parseRequestLine(requestLines);

        HttpMethod method = HttpRequestUtils.parseHttpMethod(tokens[0]);
        String path = HttpRequestUtils.parsePath(tokens[1]);
        Map<String, String> queries = HttpRequestUtils.parseQueries(tokens[1]);
        Map<String, String> headers = HttpRequestUtils.parseHeaders(requestLines.subList(1, requestLines.size()));

        return new HttpRequest(method, path, queries, headers);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getAccept() {
        String accepts = headers.getOrDefault("Accept", "");
        return HttpRequestUtils.parseAccepts(accepts);
    }

    public int getContentLength() {
        String contentLength = headers.getOrDefault("Content-Length", "0");
        return Integer.parseInt(contentLength);
    }

    public Map<String, String> getParams() {
        return Collections.unmodifiableMap(params);
    }

    public void setParams(String body) {
        this.params = HttpRequestUtils.parseRequestBody(body);
    }
}
