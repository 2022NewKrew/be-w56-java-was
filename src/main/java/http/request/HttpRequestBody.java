package http.request;

import util.HttpRequestUtils;

import java.util.Map;

public class HttpRequestBody {
    // 구현 예정
    private final Map<String, String> body;

    public HttpRequestBody(String requestBodyString) {
        this.body = HttpRequestUtils.parseQueryString(requestBodyString);
    }

    public String getParam(String key) {
        return body.get(key);
    }
}
