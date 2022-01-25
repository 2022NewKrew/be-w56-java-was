package webserver.http;

import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String path;
    private final Map<String, String> parameters;
    private final String version;

    public HttpRequest(String method, String path, Map<String, String> parameters, String version) {
        this.method = method;
        this.path = path;
        this.parameters = parameters;
        this.version = version;
    }

    public HttpRequest(String line) {
        String[] tokens = line.split(" ");
        this.method = tokens[0];
        String[] pathAndParams = tokens[1].split("\\?");
        this.path = pathAndParams[0];
        if (pathAndParams.length > 1) {
            this.parameters = HttpRequestUtils.parseQueryString(pathAndParams[1]);
        } else {
            this.parameters = new HashMap<>();
        }
        this.version = tokens[2];
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
