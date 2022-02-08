package http;

import http.header.HttpHeaders;
import http.header.HttpProtocolVersion;
import lombok.Getter;

@Getter
public abstract class HttpMessage {

    protected final HttpProtocolVersion protocolVersion;
    protected final HttpHeaders headers;

    public HttpMessage(HttpProtocolVersion protocolVersion, HttpHeaders headers) {
        this.protocolVersion = protocolVersion;
        this.headers = headers;
    }
}
