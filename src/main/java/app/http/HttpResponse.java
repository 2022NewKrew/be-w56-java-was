package app.http;

import static util.Constant.BLANK;
import static util.HttpRequestUtils.Pair;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HttpResponse {
    private final HttpVersion version;
    private final HttpStatus status;
    private final HttpHeader headers;
    private Cookie cookie;
    private final byte[] body;
    private final Integer bodyLength;

    @Builder
    public HttpResponse(HttpVersion version, HttpStatus status, List<Pair> pairs, byte[] body) {
        this.version = version;
        this.status = status;
        this.headers = HttpHeader.of(pairs);
        this.body = body;
        this.bodyLength = body.length;
    }

    public String getStatusLine() {
        return version.version() + BLANK + status.status();
    }

    public String responseHeader() {
        return headers.headers();
    }
}
