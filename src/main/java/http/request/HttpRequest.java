package http.request;

import http.HttpHeaders;

import java.io.IOException;
import java.util.Optional;

public class HttpRequest {

    private HttpRequestLine requestLine;
    private HttpHeaders requestHeader;
    private String body;

    public HttpRequest(HttpRequestLine httpRequestLine, HttpHeaders httpRequestHeader, String body) throws IOException {
        requestLine = httpRequestLine;
        requestHeader = httpRequestHeader;
        this.body = body;
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getParam(String paramName) {
        return requestLine.getParam(paramName);
    }

    public Optional<String> getHeader(String headerName) {
        return requestHeader.getHeader(headerName);
    }

    public boolean isMethod(HttpMethod method) {
        return requestLine.isMethod(method);
    }

    public String getBody() {
        return body;
    }

    public void loggingRequestHeader() {
//        for (String header : requestHeader.keySet()) {
//            log.info(header + ": " + requestHeader.get(header));
//        }
    }
}
