package model.builder;

import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.HttpResponse;
import util.HttpResponseHeader;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;

public class UserCreateResponseBuilder extends ResponseBuilder {
    @Override
    public HttpResponse build(RequestHeader requestHeader) throws IOException, SQLException {
        return HttpResponse.builder()
                .locationUri(Links.MAIN)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model))
                .htmlResponseHeader(HttpResponseHeader.REDIRECT_302)
                .accept(requestHeader.getAccept())
                .build();
    }
}
