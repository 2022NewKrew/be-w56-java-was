package model.builder;

import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.HtmlResponse;
import util.HtmlResponseHeader;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;

public class ExceptionResponseBuilder extends ResponseBuilder {
    @Override
    public HtmlResponse build(RequestHeader requestHeader) throws IOException, SQLException {
        return HtmlResponse.builder()
                .uri(Links.ERROR)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.ERROR), model))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302)
                .accept("text/html")
                .build();
    }
}
