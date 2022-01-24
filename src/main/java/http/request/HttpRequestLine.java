package http.request;

import lombok.Getter;

import java.util.HashMap;
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
        return url.substring(0, url.indexOf("?"));
    }

    private Map<String, String> extractParams(String url) {
        Map<String, String> paramMap = new HashMap<>();
        String queryString = url.substring(url.indexOf("?") + 1);
        String[] params = queryString.split("&");
        for (String param : params) {
            String[] splits = param.split("=");
            paramMap.put(splits[0], splits[1]);
        }
        return paramMap;
    }

    public String getParam(String name) {
        return params.get(name);
    }
}
