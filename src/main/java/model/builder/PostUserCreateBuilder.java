package model.builder;

import model.RequestHeader;
import model.ResponseHeader;
import util.HtmlResponseHeader;
import util.Links;

public class PostUserCreateBuilder extends ResponseBuilder {
    @Override
    public ResponseHeader build(RequestHeader requestHeader) {
        return ResponseHeader.builder()
                .uri(Links.MAIN)
                .body(readBody(Links.MAIN))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302)
                .accept(requestHeader.getAccept())
                .build();
    }
}
