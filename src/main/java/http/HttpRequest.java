package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> queries;
    private final Map<String, String> headers;
    private final Map<String, String> params;

    private HttpRequest(HttpMethod method, String path, Map<String, String> queries, Map<String, String> headers,
            Map<String, String> params) {
        this.method = method;
        this.path = path;
        this.queries = queries;
        this.headers = headers;
        this.params = params;
    }

    public static HttpRequest from(BufferedReader br) throws IOException {
        String requestLine = IOUtils.readRequestLine(br);
        String[] tokens = HttpRequestUtils.parseRequestLine(requestLine);
        HttpMethod method = HttpRequestUtils.parseHttpMethod(tokens[0]);
        String path = HttpRequestUtils.parsePath(tokens[1]);
        Map<String, String> queries = HttpRequestUtils.parseQueries(tokens[1]);

        List<String> headerStrings = IOUtils.readHttpHeaders(br);
        Map<String, String> headers = HttpRequestUtils.parseHeaders(headerStrings);

        int contentLength = getContentLength(headers);
        String body = IOUtils.readData(br, contentLength);
        Map<String, String> params = HttpRequestUtils.parseRequestBody(body);

        return new HttpRequest(method, path, queries, headers, params);
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

    private static int getContentLength(Map<String, String> headers) {
        String contentLength = headers.getOrDefault("Content-Length", "0");
        return Integer.parseInt(contentLength);
    }

    public Map<String, String> getParams() {
        return Collections.unmodifiableMap(params);
    }
}
