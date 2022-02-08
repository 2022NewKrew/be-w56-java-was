package model.builder;

import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.ResponseHeader;
import util.HtmlResponseHeader;
import util.Links;

import java.io.IOException;

public class ExceptionRequestBuilder extends ResponseBuilder {
    @Override
    public ResponseHeader build(RequestHeader requestHeader) throws IOException {
        return ResponseHeader.builder()
                .uri(Links.ERROR)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.ERROR), model))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302)
                .accept("text/html")
                .build();
    }
}
