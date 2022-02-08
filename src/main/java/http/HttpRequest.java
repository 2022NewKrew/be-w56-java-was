package http;

import lombok.Builder;
import util.HttpRequestUtils;

public class HttpRequest {

    private final HttpMethod method;

    private final String path;
    private final String version;

    private final HttpHeader header;

    private final RequestParameters pathParameters;
    private final RequestParameters bodyParameters;

    @Builder
    public HttpRequest(HttpMethod method, String path, String version, HttpHeader header,
        RequestParameters pathParameters, RequestParameters bodyParameters) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.header = header;
        this.pathParameters = pathParameters;
        this.bodyParameters = bodyParameters;
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String key) {
        return header.get(key);
    }

    public String getPathParameter(String key) {
        return pathParameters.get(key);
    }

    public String getBodyParameter(String key) {
        return bodyParameters.get(key);
    }

    public boolean isGet() {
        return HttpMethod.GET.equals(method);
    }

    public boolean isPost() {
        return HttpMethod.POST.equals(method);
    }

    public boolean isPut() {
        return HttpMethod.PUT.equals(method);
    }

    public boolean isDelete() {
        return HttpMethod.DELETE.equals(method);
    }

    public String getVersion() {
        return version;
    }

    public boolean isLogined() {
        return Boolean.parseBoolean(
            HttpRequestUtils.parseCookies(header.get("Cookie")).getOrDefault("logined", "false"));
    }
}
