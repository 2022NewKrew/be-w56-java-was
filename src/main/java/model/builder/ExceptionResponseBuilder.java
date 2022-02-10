package model.builder;

import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.HttpResponse;
import util.HttpResponseHeader;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;

public class ExceptionResponseBuilder extends ResponseBuilder {
    @Override
    public HttpResponse build(RequestHeader requestHeader) throws IOException, SQLException {
        return HttpResponse.builder()
                .locationUri(Links.ERROR)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.ERROR), model))
                .htmlResponseHeader(HttpResponseHeader.REDIRECT_302)
                .accept("text/html")
                .build();
    }
}
