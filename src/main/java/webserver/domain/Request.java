package webserver.domain;

import java.util.List;
import lombok.Builder;
import util.HttpRequestUtils.Pair;

public class Request {

    private final StartLine startLine;
    private final Headers headers;

    @Builder
    private Request(StartLine startLine, Headers headers) {
        this.startLine = startLine;
        this.headers = headers;
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

    public String getRequestUrl() {
        return startLine.getRequestUrl();
    }
}
