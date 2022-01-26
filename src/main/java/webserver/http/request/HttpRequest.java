package webserver.http.request;

import java.net.URI;
import java.util.Map;
import util.HttpRequestUtils;

public class HttpRequest {

    private final HttpRequestLine requestLine;
    private final HttpRequestHeaders requestHeaders;
    private final HttpRequestBody requestBody;

    public HttpRequest(HttpRequestLine requestLine, HttpRequestHeaders requestHeaders, HttpRequestBody requestBody) {
        this.requestLine = requestLine;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
    }

    public String getRequestBody() {
        return requestBody.getBody();
    }

    public String getHttpVersion() {
        return requestLine.getHttpVersion();
    }

    public URI getUri() {
        return requestLine.getUri();
    }

    public Method getMethod() {
        return requestLine.getMethod();
    }

    public Map<String, String> getQueryData() {
        Method method = getMethod();
        return method == Method.GET ? getQueryDataFromUri() : getQueryDataFromBody();
    }

    private Map<String, String> getQueryDataFromUri() {
        URI uri = getUri();
        return HttpRequestUtils.parseQueryString(uri.getQuery());
    }

    private Map<String, String> getQueryDataFromBody() {
        String body = getRequestBody();
        return HttpRequestUtils.parseQueryString(body);
    }
}
