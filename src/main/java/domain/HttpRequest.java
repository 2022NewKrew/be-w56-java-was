package domain;

import java.util.Map;

public class HttpRequest {

    private final RequestLine requestLine;
    private final HttpHeaders headers;

    public HttpRequest(String requestLine, Map<String, String> headers) {
        this.requestLine = new RequestLine(requestLine);
        this.headers = new HttpHeaders(headers);
    }

    public String getRequestPath() {
        return requestLine.getPath();
    }

    public String getValueByHeader(String header) {
        return headers.getValueByHeader(header);
    }
}
