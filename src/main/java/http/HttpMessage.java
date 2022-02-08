package http;

import http.cookie.Cookie;
import http.header.HttpHeaders;
import http.header.HttpProtocolVersion;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class HttpMessage {

    protected final HttpProtocolVersion protocolVersion;
    protected final HttpHeaders headers;
    protected final List<Cookie> cookies;

    public HttpMessage(HttpProtocolVersion protocolVersion, HttpHeaders headers, List<Cookie> cookies) {
        this.protocolVersion = protocolVersion;
        this.headers = headers;
        this.cookies = cookies;
    }
}
