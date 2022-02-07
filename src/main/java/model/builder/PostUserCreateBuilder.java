package model.builder;

import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.ResponseHeader;
import util.HtmlResponseHeader;
import util.Links;

import java.io.IOException;

public class PostUserCreateBuilder extends ResponseBuilder {
    @Override
    public ResponseHeader build(RequestHeader requestHeader) throws IOException {
        return ResponseHeader.builder()
                .uri(Links.MAIN)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN)))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302)
                .accept(requestHeader.getAccept())
                .build();
    }
}
