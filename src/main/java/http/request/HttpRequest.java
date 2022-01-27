package http.request;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpMethod;

public class HttpRequest {

    private final RequestHeader header;
    private final RequestBody body;

    public HttpRequest(String header, String body) {
        this.header = new RequestHeader(header);
        this.body = new RequestBody(body);
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
}
