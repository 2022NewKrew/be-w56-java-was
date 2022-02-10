package model.builder;

import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.HttpResponse;
import model.User;
import service.UserService;
import util.HttpResponseHeader;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserListResponseBuilder extends ResponseBuilder {
    private final UserService userService;

    public UserListResponseBuilder() {
        userService = UserService.getInstance();
    }

    @Override
    public HttpResponse build(RequestHeader requestHeader) throws IOException, SQLException {
        if (requestHeader.existCookie("logined") && requestHeader.getCookie("logined").equals("true")) {
            return logined(requestHeader);
        }
        return notLogined(requestHeader);
    }

    private HttpResponse logined(RequestHeader requestHeader) throws IOException, SQLException {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);

        return HttpResponse.builder()
                .locationUri(Links.USER_LIST)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.USER_LIST), model))
                .htmlResponseHeader(HttpResponseHeader.RESPONSE_200)
                .accept(requestHeader.getAccept())
                .build();
    }

    private HttpResponse notLogined(RequestHeader requestHeader) throws IOException {
        return HttpResponse.builder()
                .locationUri(Links.MAIN)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model))
                .htmlResponseHeader(HttpResponseHeader.REDIRECT_302)
                .accept(requestHeader.getAccept())
                .build();
    }
}
