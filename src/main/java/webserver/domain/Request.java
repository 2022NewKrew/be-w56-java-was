package webserver.domain;

import java.util.List;
import lombok.Builder;
import util.HttpRequestUtils.Pair;

public class Request {

    private final StartLine startLine;
    private final Headers headers;

    @Builder
    private Request(StartLine startLine, Headers headers) {
        this.startLine = new StartLine(startLine);
        this.headers = new Headers(headers);
    }

    public static Request createRequest(String line, List<Pair> headerPairs) {
        StartLine startLine = StartLine.createStartLine(line);
        Headers headers = Headers.createRequestHeader(headerPairs);
        return Request.builder()
            .startLine(startLine)
            .headers(headers)
            .build();
    }

    public String getHeaderAttribute(String key) {
        return headers.getHeaderAttribute(key);
    }

    public String getQueryStringAttribute(String key) {
        return startLine.getQueryStringAttribute(key);
    }

    public String getPath() {
        return startLine.getPath();
    }

    public String getHttpMethod(){
        return startLine.getHttpMethod();
    }

    public boolean isFile() {
        return startLine.isFile();
    }

    public boolean startsWith(String path) {
        return startLine.startsWith(path);
    }
}
