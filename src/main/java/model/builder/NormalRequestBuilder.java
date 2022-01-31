package model.builder;

import model.RequestHeader;
import model.ResponseHeader;
import util.HtmlResponseHeader;
import util.Links;

import java.io.IOException;

public class NormalRequestBuilder extends ResponseBuilder {
    @Override
    public ResponseHeader build(RequestHeader requestHeader) throws IOException {
        String uri = requestHeader.getHeader("uri");
        if (uri.equals("/")) {
            uri = Links.MAIN;
        }

        if (!uri.contains(".")) {
            uri += ".html";
        }

        return ResponseHeader.builder()
                .uri(uri)
                .htmlResponseHeader(HtmlResponseHeader.RESPONSE_200)
                .body(readBody(uri))
                .accept(requestHeader.getAccept())
                .build();
    }
}
