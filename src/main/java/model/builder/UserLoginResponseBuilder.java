package model.builder;

import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.HtmlResponse;
import service.UserService;
import util.HtmlResponseHeader;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;

public class UserLoginResponseBuilder extends ResponseBuilder {
    private final UserService userService;

    public UserLoginResponseBuilder() {
        userService = UserService.getInstance();
    }

    @Override
    public HtmlResponse build(RequestHeader requestHeader) throws IOException, SQLException {
        String userId = requestHeader.getParameter("userId");
        String password = requestHeader.getParameter("password");
        if (userService.login(userId, password)) {
            return buildSuccessLogin(requestHeader);
        }
        return buildFailLogin(requestHeader);
    }

    private HtmlResponse buildSuccessLogin(RequestHeader requestHeader) throws IOException {
        return HtmlResponse.builder()
                .locationUri(Links.MAIN)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302_WITH_LOGIN_COOKIE)
                .accept(requestHeader.getAccept())
                .build();
    }

    private HtmlResponse buildFailLogin(RequestHeader requestHeader) throws IOException {
        return HtmlResponse.builder()
                .locationUri(Links.LOGIN_FAILED)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.LOGIN_FAILED), model))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302)
                .accept(requestHeader.getAccept())
                .build();
    }
}
