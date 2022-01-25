package http;

import java.util.Map;
import lombok.Builder;

public class Request {

    private final Method method;
    private final String target;
    private final String version;
    private final Map<String, String> headers;

    @Builder
    public Request(Method method, String target, String version, Map<String, String> headers) {
        this.method = method;
        this.target = target;
        this.version = version;
        this.headers = headers;
    }

    public String getTarget() {
        return target;
    }

    public int getHeaderSize() {
        return headers.size();
    }

    public String getHeader(String key) {
        if (!headers.containsKey(key)) {
            return "";
        }
        return headers.get(key);
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
