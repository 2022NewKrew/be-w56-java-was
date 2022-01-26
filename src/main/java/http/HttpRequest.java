package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final Queries queries;
    private final HttpHeaders headers;
    private final RequestBody body;

    private HttpRequest(HttpMethod method, String path, Queries queries, HttpHeaders headers, RequestBody body) {
        this.method = method;
        this.path = path;
        this.queries = queries;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest from(BufferedReader br) throws IOException {
        String requestLine = IOUtils.readRequestLine(br);
        String[] tokens = HttpRequestUtils.parseRequestLine(requestLine);

        HttpMethod method = HttpRequestUtils.parseHttpMethod(tokens[0]);
        String path = HttpRequestUtils.parsePath(tokens[1]);
        Queries queries = HttpRequestUtils.parseQueries(tokens[1]);

        List<String> headerStrings = IOUtils.readHttpHeaders(br);
        HttpHeaders headers = HttpRequestUtils.parseHeaders(headerStrings);

        String bodyString = IOUtils.readData(br, headers.getContentLength());
        RequestBody body = HttpRequestUtils.parseRequestBody(bodyString);

        return new HttpRequest(method, path, queries, headers, body);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public RequestBody getBody() {
        return body;
    }
}
