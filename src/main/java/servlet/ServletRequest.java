package servlet;

import http.Cookie;
import http.HttpMethod;

import java.util.Map;

public class ServletRequest {
    private HttpMethod method;
    private String path;
    private Map<String, String> parameters;
    private Cookie cookie;

    public ServletRequest(HttpMethod method, String path, Map<String, String> parameters, Cookie cookie) {
        this.method = method;
        this.path = path;
        this.parameters = parameters;
    }

    public MappingKey createMappingKey() {
        return new MappingKey(method.toString() + path);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
