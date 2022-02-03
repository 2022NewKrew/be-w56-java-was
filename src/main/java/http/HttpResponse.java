package http;

import java.util.HashMap;
import java.util.stream.Collectors;
import lombok.Builder;

public class HttpResponse {

    private final HttpStatus status;

    private final HttpHeader header;
    private final byte[] responseBody;

    @Builder
    public HttpResponse(HttpStatus status, HttpHeader header, byte[] responseBody) {
        this.status = status;
        this.header = header == null ? HttpHeader.of(new HashMap<>()) : header;
        this.responseBody = responseBody == null ? new byte[0] : responseBody;
    }

    public String respondStatus() {
        return status.value();
    }

    public String respondHeader() {
        return header.toFormattedStrings().stream()
            .collect(Collectors.joining(System.lineSeparator()));
    }

    public byte[] getResponseBody() {
        return responseBody;
    }
}
