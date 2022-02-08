package model;

import java.util.Map;

public class HttpRedirectionResponse extends HttpResponse {

    public static HttpRedirectionResponse of(HttpStatus httpStatus, String url) {
        StatusLine statusLine = new StatusLine(HttpVersion.HTTP_1_1.getVersion(), httpStatus.getCode(),
                httpStatus.getMessage());
        Map<String, String> headerKeyMap = Map.of("Location", url);
        return new HttpRedirectionResponse(statusLine, new Header(headerKeyMap));
    }

    public HttpRedirectionResponse(StatusLine statusLine, Header header) {
        super(statusLine, header);
    }
}
