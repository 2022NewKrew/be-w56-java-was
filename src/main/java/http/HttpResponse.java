package http;

import java.util.stream.Collectors;
import lombok.Builder;

public class HttpResponse {

    private final HttpStatus status;

    private final HttpHeader header;
    private final byte[] responseBody;

    @Builder
    public HttpResponse(HttpStatus status, HttpHeader header, byte[] responseBody) {
        this.status = status;
        this.header = header;
        this.responseBody = responseBody;
    }

    public String respondStatus() {
        return status.value();
    }

    public String respondHeader() {
        return header.toStringList().stream()
            .collect(Collectors.joining(System.lineSeparator()));
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public boolean hasError() {
        return status.isClientError() || status.isServerError();
    }
}
