package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import util.HttpRequestUtils.Pair;

public class Request {

    private final String method;
    private final String target;
    private final String version;
    private final Map<String, String> headers;

    @Builder
    public Request(String requestLine, List<Pair> pairs) {
        String[] tokens = requestLine.split(" ");
        this.method = tokens[0];
        this.target = tokens[1];
        this.version = tokens[2];

        headers = new HashMap<>();
        pairs.forEach(pair -> headers.put(pair.getKey(), pair.getValue()));
    }

    public String getMethod() {
        return method;
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
}
