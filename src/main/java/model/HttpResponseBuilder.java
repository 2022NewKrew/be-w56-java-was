package model;

import util.HttpResponseHeader;

public class HttpResponseBuilder {
    private HttpResponseBuilder() {

    }

    public static HttpResponse build(String locationUri, byte[] body,
                                     HttpResponseHeader httpResponseHeader, String accept) {
        return HttpResponse.builder()
                .locationUri(locationUri)
                .body(body)
                .htmlResponseHeader(httpResponseHeader)
                .accept(accept)
                .build();
    }
}
