package http.request;

import lombok.Getter;
import util.HttpRequestUtils;

import java.util.Map;

@Getter
public class HttpRequestLine {
    private String method;
    private String url;
    private String version;
    private Map<String, String> params;

    public HttpRequestLine(String requestLine) {
        String[] splits = requestLine.split(" ");
        if (splits.length != 3) {
            throw new IllegalArgumentException("invalid http request line");
        }
        this.method = splits[0];
        this.url = extractUrl(splits[1]);
        this.params = extractParams(splits[1]);
        this.version = splits[2];
    }

    private String extractUrl(String url) {
        return url.contains("?") ? url.substring(0, url.indexOf("?")) : url;
    }

    private Map<String, String> extractParams(String url) {
        String queryString = url.contains("?") ? url.substring(url.indexOf("?") + 1) : "";
        return HttpRequestUtils.parseQueryString(queryString);
    }

    public String getParam(String name) {
        return params.get(name);
    }

    public boolean containsParam(String name) {
        return params.containsKey(name);
    }
}
