package http;

import lombok.Builder;
import lombok.Getter;

@Getter
public abstract class HttpMessage {

    protected final String protocolVersion;
    protected final HttpHeaders headers;

    @Builder
    public HttpMessage(String protocolVersion, HttpHeaders headers) {
        this.protocolVersion = protocolVersion;
        this.headers = headers;
    }
}
