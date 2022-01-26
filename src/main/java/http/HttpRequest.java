package http;

import lombok.Builder;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final String version;
    private final RequestParams requestParams;

    private final HttpHeader header;
    private final RequestBody requestBody;

    @Builder
    public HttpRequest(HttpMethod method, String path, String version, RequestParams requestParams,
        HttpHeader header, RequestBody requestBody) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.requestParams = requestParams;
        this.header = header;
        this.requestBody = requestBody;
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String key) {
        return header.get(key);
    }

    public String getRequestParam(String key) {
        return requestParams.get(key);
    }

    public String getRequestBody(String key) {
        return requestBody.get(key);
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
}
