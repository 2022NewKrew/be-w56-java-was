package model;

import java.util.Map;

public class HttpRedirectionResponse extends HttpResponse {

    public static HttpRedirectionResponse of(HttpStatus httpStatus, String url, String cookie) {
        StatusLine statusLine = new StatusLine(HttpVersion.HTTP_1_1, httpStatus.getCode(),
                httpStatus.getMessage());
        Map<HttpHeader, String> headerKeyMap = Map.of(HttpHeader.LOCATION, url, HttpHeader.SET_COOKIE, cookie + "; Path=/");
        return new HttpRedirectionResponse(statusLine, new Header(headerKeyMap));
    }

    public HttpRedirectionResponse(StatusLine statusLine, Header header) {
        super(statusLine, header);
    }
}
