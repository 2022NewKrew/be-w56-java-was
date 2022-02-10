package model.builder;

import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.HttpResponse;
import service.UserService;
import util.HttpResponseHeader;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;

public class UserLoginResponseBuilder extends ResponseBuilder {
    private final UserService userService;

    public UserLoginResponseBuilder() {
        userService = UserService.getInstance();
    }

    @Override
    public HttpResponse build(RequestHeader requestHeader) throws IOException, SQLException {
        String userId = requestHeader.getParameter("userId");
        String password = requestHeader.getParameter("password");
        if (userService.login(userId, password)) {
            return buildSuccessLogin(requestHeader);
        }
        return buildFailLogin(requestHeader);
    }

    private HttpResponse buildSuccessLogin(RequestHeader requestHeader) throws IOException {
        return HttpResponse.builder()
                .locationUri(Links.MAIN)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model))
                .htmlResponseHeader(HttpResponseHeader.REDIRECT_302_WITH_LOGIN_COOKIE)
                .accept(requestHeader.getAccept())
                .build();
    }

    private HttpResponse buildFailLogin(RequestHeader requestHeader) throws IOException {
        return HttpResponse.builder()
                .locationUri(Links.LOGIN_FAILED)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.LOGIN_FAILED), model))
                .htmlResponseHeader(HttpResponseHeader.REDIRECT_302)
                .accept(requestHeader.getAccept())
                .build();
    }
}
