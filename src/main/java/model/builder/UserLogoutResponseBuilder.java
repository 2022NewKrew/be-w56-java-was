package model.builder;

import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.HtmlResponse;
import util.HtmlResponseHeader;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;

public class UserLogoutResponseBuilder extends ResponseBuilder {

    @Override
    public HtmlResponse build(RequestHeader requestHeader) throws IOException, SQLException {
        return HtmlResponse.builder()
                .locationUri(Links.MAIN)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302_WITH_LOGOUT_COOKIE)
                .accept(requestHeader.getAccept())
                .build();
    }
}
