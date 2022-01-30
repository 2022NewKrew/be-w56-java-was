package servlet;

import http.HttpMethod;

import java.util.Map;

public class ServletRequest {
    HttpMethod method;
    String path;
    Map<String, String> parameters;

    public ServletRequest(HttpMethod method, String path, Map<String, String> parameters) {
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
