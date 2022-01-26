package http;

import java.util.Map;
import lombok.Builder;

public class Request {

    private final Method method;
    private final String target;
    private final String version;
    private final String body;
    private final Map<String, String> headers;

    @Builder
    public Request(Method method, String target, String version, Map<String, String> headers,
        String body) {
        this.method = method;
        this.target = target;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getTarget() {
        return target;
    }

    public String getVersion() {
        return version;
    }

    public int getHeaderSize() {
        return headers.size();
    }

    public String getHeader(String key) {
        return headers.getOrDefault(key, "");
    }

    public boolean isGet() {
        return method.equals(Method.GET);
    }

    public boolean isPost() {
        return method.equals(Method.POST);
    }

    public boolean isPut() {
        return method.equals(Method.PUT);
    }

    public boolean isDelete() {
        return method.equals(Method.DELETE);
    }
}
