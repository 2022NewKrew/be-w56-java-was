package webserver.http;

import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final Map<String, String> parameters;
    private final String version;

    public HttpRequest(String method, String path, Map<String, String> headers, Map<String, String> parameters, String version) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.parameters = parameters;
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getVersion() {
        return version;
    }
}
