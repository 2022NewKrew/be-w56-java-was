package model.builder;

import db.DataBase;
import dynamic.DynamicHtmlBuilder;
import dynamic.DynamicModel;
import model.RequestHeader;
import model.ResponseHeader;
import model.User;
import service.UserService;
import util.HtmlResponseHeader;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GetUserListBuilder extends ResponseBuilder {
    private final UserService userService;

    public GetUserListBuilder() {
        userService = UserService.getInstance();
    }

    @Override
    public ResponseHeader build(RequestHeader requestHeader) throws IOException, SQLException {
        if (requestHeader.existCookie("logined") && requestHeader.getCookie("logined").equals("true")) {
            return logined(requestHeader);
        }
        return notLogined(requestHeader);
    }

    private ResponseHeader logined(RequestHeader requestHeader) throws IOException, SQLException {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);

        return ResponseHeader.builder()
                .uri(Links.USER_LIST)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.USER_LIST), model))
                .htmlResponseHeader(HtmlResponseHeader.RESPONSE_200)
                .accept(requestHeader.getAccept())
                .build();
    }

    private ResponseHeader notLogined(RequestHeader requestHeader) throws IOException {
        return ResponseHeader.builder()
                .uri(Links.MAIN)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302)
                .accept(requestHeader.getAccept())
                .build();
    }
}
