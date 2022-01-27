package http.request;

import exception.BadRequestException;
import java.util.Map;
import webserver.HttpMethod;

public class HttpRequest {

    private final RequestStartLine startLine;
    private final RequestHeader header;
    private final RequestBody body;

    public HttpRequest(RequestStartLine startLine, RequestHeader header, RequestBody body) {
        try {
            this.startLine = startLine;
            this.header = header;
            this.body = body;
        } catch (Exception exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    public String getUrl() {
        return startLine.getUrl();
    }

    public Map<String, String> getQuery() {
        return startLine.getQuery();
    }

    public HttpMethod getMethod() {
        return startLine.getMethod();
    }

    public Map<String, String> getBodyData() {
        return body.getBodyData();
    }
}
