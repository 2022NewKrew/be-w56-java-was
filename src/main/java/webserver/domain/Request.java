package webserver.domain;

import java.util.Map;
import lombok.Builder;

public class Request {

    private final StartLine startLine;
    private final Headers headers;
    private final Data body;

    @Builder
    private Request(StartLine startLine, Headers headers, Data body) {
        this.startLine = new StartLine(startLine);
        this.headers = new Headers(headers);
        this.body = body;
    }

    public static Request createRequest(String line, Map<String, String> headers, String bodyString) {
        return Request.builder()
            .startLine(StartLine.createStartLine(line))
            .headers(Headers.createRequestHeader(headers))
            .body(Data.createData(bodyString))
            .build();
    }

    public String getHeaderAttribute(String key) {
        return headers.getHeaderAttribute(key);
    }

    public String getQueryStringAttribute(String key) {
        return startLine.getQueryStringAttribute(key);
    }

    public String getBodyAttribute(String key) {
        return body.getDataAttribute(key);
    }

    public String getPath() {
        return startLine.getPath();
    }

    public String getHttpMethod() {
        return startLine.getHttpMethod();
    }

    public boolean isFile() {
        return startLine.isFile();
    }

    public boolean startsWith(String path) {
        return startLine.startsWith(path);
    }
}
