package http;

import http.header.HttpHeaders;
import lombok.Getter;

@Getter
public abstract class HttpMessage {

    protected final String protocolVersion;
    protected final HttpHeaders headers;

    public HttpMessage(String protocolVersion, HttpHeaders headers) {
        this.protocolVersion = protocolVersion;
        this.headers = headers;
    }
}
