package webserver.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class HttpRequest {
    private HttpRequestMethod method;
    private String target;
    private String version;

    @Getter(AccessLevel.NONE)
    private final Map<String, String> headers = new HashMap<>();

    @Builder
    public HttpRequest(HttpRequestMethod method, String target, String version) {
        this.method = method;
        this.target = target;
        this.version = version;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s %s %s\r\n", method, target, version));
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(String.format("%s: %s\r\n", entry.getKey(), entry.getValue()));
        }

        return sb.toString();
    }
}
