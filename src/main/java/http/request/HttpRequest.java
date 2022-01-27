package http.request;

import exception.BadRequestException;
import java.util.Map;
import webserver.HttpMethod;

public class HttpRequest {

    private final RequestHeader header;
    private final RequestBody body;

    public HttpRequest(String header, String body) {
        try {
            this.header = new RequestHeader(header);
            this.body = new RequestBody(body);
        } catch (Exception exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    public String getUrl() {
        return header.getUrl();
    }

    public Map<String, String> getQuery() {
        return header.getQuery();
    }

    public HttpMethod getMethod() {
        return header.getMethod();
    }

    public Map<String, String> getBodyData() {
        return body.getBodyData();
    }
}
