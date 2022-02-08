package webapplication.utils;

import http.HttpBody;
import http.HttpHeaders;
import http.HttpStatus;
import http.MultiValueMap;
import http.response.HttpResponse;
import http.response.StatusLine;

public class HttpResponseUtils {

    public static HttpResponse createInternalError(String protocolVersion) {
        return HttpResponse.Builder.newInstance()
                .statusLine(StatusLine.of(protocolVersion, HttpStatus.INTERNAL_SERVER_ERROR))
                .build();
    }

    public static HttpResponse createOk(String protocolVersion, HttpHeaders httpHeaders, byte[] body) {
        return HttpResponse.Builder.newInstance()
                .statusLine(StatusLine.of(protocolVersion, HttpStatus.OK))
                .headers(httpHeaders)
                .body(HttpBody.of(body))
                .build();
    }

    public static HttpResponse createRedirect(String protocolVersion, String location) {
        MultiValueMap<String, String> headers = new MultiValueMap<>();
        headers.add(HttpHeaders.LOCATION, location);
        return HttpResponse.Builder.newInstance()
                .statusLine(StatusLine.of(protocolVersion, HttpStatus.FOUND))
                .headers(HttpHeaders.of(headers))
                .build();
    }

}
