package model.builder;

import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.ResponseHeader;
import util.HtmlResponseHeader;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;

public class UserLogoutResponseBuilder extends ResponseBuilder {

    @Override
    public ResponseHeader build(RequestHeader requestHeader) throws IOException, SQLException {
        return ResponseHeader.builder()
                .uri(Links.MAIN)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302_WITH_LOGOUT_COOKIE)
                .accept(requestHeader.getAccept())
                .build();
    }
}
