package model.builder;

import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.HttpResponse;
import util.HtmlResponseHeader;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;

public class UserLogoutResponseBuilder extends ResponseBuilder {

    @Override
    public HttpResponse build(RequestHeader requestHeader) throws IOException, SQLException {
        return HttpResponse.builder()
                .locationUri(Links.MAIN)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302_WITH_LOGOUT_COOKIE)
                .accept(requestHeader.getAccept())
                .build();
    }
}
