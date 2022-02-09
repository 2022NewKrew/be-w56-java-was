package model.builder;

import db.DataBase;
import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.ResponseHeader;
import service.UserService;
import util.HtmlResponseHeader;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;

public class PostUserLoginBuilder extends ResponseBuilder {
    private final UserService userService;

    public PostUserLoginBuilder() {
        userService = UserService.getInstance();
    }

    @Override
    public ResponseHeader build(RequestHeader requestHeader) throws IOException, SQLException {
        String userId = requestHeader.getParameter("userId");
        String password = requestHeader.getParameter("password");
        if (userService.login(userId, password)) {
            return buildSuccessLogin(requestHeader);
        }
        return buildFailLogin(requestHeader);
    }

    private ResponseHeader buildSuccessLogin(RequestHeader requestHeader) throws IOException {
        return ResponseHeader.builder()
                .uri(Links.MAIN)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302_WITH_LOGIN_COOKIE)
                .accept(requestHeader.getAccept())
                .build();
    }

    private ResponseHeader buildFailLogin(RequestHeader requestHeader) throws IOException {
        return ResponseHeader.builder()
                .uri(Links.LOGIN_FAILED)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.LOGIN_FAILED), model))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302)
                .accept(requestHeader.getAccept())
                .build();
    }
}
