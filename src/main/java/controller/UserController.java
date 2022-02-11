package controller;

import dynamic.DynamicHtmlBuilder;
import dynamic.DynamicModel;
import model.HttpResponse;
import model.HttpResponseBuilder;
import model.RequestHeader;
import service.UserService;
import util.ControllerUtils;
import util.HttpResponseHeader;
import util.IOUtils;
import util.Links;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class UserController {
    private static final UserService userService = UserService.getInstance();

    private UserController() {

    }

    public static HttpResponse controlRequest(RequestHeader requestHeader, String uri, String method) throws Exception {
        DynamicModel model = new DynamicModel();

        if (uri.startsWith("/user/create") && method.equals("POST")) {
            return postUserCreate(requestHeader, model);
        }

        if (uri.startsWith("/user/login") && method.equals("POST")) {
            return postUserLogin(requestHeader, model);
        }

        if (uri.startsWith("/user/list") && method.equals("GET")) {
            return getUserList(requestHeader, model);
        }

        if (uri.startsWith("/user/logout") && method.equals("GET")) {
            return postUserLogout(requestHeader, model);
        }

        return null;
    }

    private static HttpResponse postUserLogout(RequestHeader requestHeader, DynamicModel model) {
        return HttpResponseBuilder.build(
                Links.MAIN,
                "".getBytes(StandardCharsets.UTF_8),
                HttpResponseHeader.REDIRECT_302_WITH_LOGOUT_COOKIE,
                requestHeader.getAccept()
        );
    }

    private static HttpResponse postUserLogin(RequestHeader requestHeader, DynamicModel model) throws SQLException {
        String userId = requestHeader.getParameter("userId");
        String password = requestHeader.getParameter("password");
        if (userService.login(userId, password)) {
            return redirectWithLogin();
        }
        return ControllerUtils.redirect(Links.LOGIN_FAILED);
    }

    private static HttpResponse redirectWithLogin() {
        return HttpResponseBuilder.build(
                Links.MAIN,
                "".getBytes(StandardCharsets.UTF_8),
                HttpResponseHeader.REDIRECT_302_WITH_LOGIN_COOKIE,
                "text/html"
        );
    }

    private static HttpResponse getUserList(RequestHeader requestHeader, DynamicModel model)
            throws SQLException, IOException {
        if (requestHeader.existCookie("logined") && requestHeader.getCookie("logined").equals("true")) {
            model.addAttribute("users", userService.findAll());
            return logined(requestHeader, model);
        }
        return ControllerUtils.redirect(Links.MAIN);
    }

    private static HttpResponse logined(RequestHeader requestHeader, DynamicModel model)
            throws IOException {
        return HttpResponseBuilder.build(
                Links.USER_LIST,
                DynamicHtmlBuilder.getDynamicHtml(IOUtils.readBody(Links.USER_LIST), model),
                HttpResponseHeader.RESPONSE_200,
                requestHeader.getAccept()
        );
    }

    private static HttpResponse postUserCreate(RequestHeader requestHeader, DynamicModel model) throws SQLException {
        userService.save(requestHeader);
        return ControllerUtils.redirect(Links.MAIN);
    }
}
