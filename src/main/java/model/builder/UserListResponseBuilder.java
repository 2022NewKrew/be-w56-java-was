package model.builder;

import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.HtmlResponse;
import model.User;
import service.UserService;
import util.HtmlResponseHeader;
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
    public HtmlResponse build(RequestHeader requestHeader) throws IOException, SQLException {
        if (requestHeader.existCookie("logined") && requestHeader.getCookie("logined").equals("true")) {
            return logined(requestHeader);
        }
        return notLogined(requestHeader);
    }

    private HtmlResponse logined(RequestHeader requestHeader) throws IOException, SQLException {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);

        return HtmlResponse.builder()
                .locationUri(Links.USER_LIST)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.USER_LIST), model))
                .htmlResponseHeader(HtmlResponseHeader.RESPONSE_200)
                .accept(requestHeader.getAccept())
                .build();
    }

    private HtmlResponse notLogined(RequestHeader requestHeader) throws IOException {
        return HtmlResponse.builder()
                .locationUri(Links.MAIN)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302)
                .accept(requestHeader.getAccept())
                .build();
    }
}
