package controller;

import dynamic.DynamicHtmlBuilder;
import dynamic.DynamicModel;
import exception.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import model.*;
import service.MemoService;
import service.UserService;
import util.HttpResponseHeader;
import util.Links;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

@Slf4j
public class RequestController {
    private static final UserService userService = UserService.getInstance();
    private static final MemoService memoService = MemoService.getInstance();

    private RequestController() {

    }

    public static HttpResponse controlRequest(RequestHeader requestHeader) throws Exception {
        String uri = requestHeader.getHeader("uri");
        String method = requestHeader.getHeader("method");
        log.info("CONTROL URI: " + uri);
        log.info("CONTROL METHOD: " + method);

        DynamicModel model = new DynamicModel();

        try {

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

            if (uri.startsWith("/memo/write") && method.equals("POST")) {
                return postMemoWrite(requestHeader, model);
            }


            if ((uri.equals("/") || uri.startsWith("/index")) && method.equals("GET")) {
                return getIndex(requestHeader, model);
            }

            return getDefault(requestHeader, model);
        } catch (Exception exception) {
            ExceptionHandler.handleException(exception, requestHeader);
            return getErrorPage(model);
        }
    }

    private static HttpResponse getErrorPage(DynamicModel model) throws IOException {
        return HttpResponseBuilder.build(
                Links.ERROR,
                DynamicHtmlBuilder.getDynamicHtml(readBody(Links.ERROR), model),
                HttpResponseHeader.REDIRECT_302,
                "text/html"
        );
    }

    private static HttpResponse getDefault(RequestHeader requestHeader, DynamicModel model) throws IOException {
        String uri = requestHeader.getHeader("uri");

        if (!uri.contains(".")) {
            uri += ".html";
        }

        byte[] body = readBody(uri);
        if (uri.contains(".html")) {
            body = DynamicHtmlBuilder.getDynamicHtml(body, model);
        }

        return HttpResponse.builder()
                .locationUri(uri)
                .htmlResponseHeader(HttpResponseHeader.RESPONSE_200)
                .body(body)
                .accept(requestHeader.getAccept())
                .build();
    }

    private static HttpResponse postUserLogout(RequestHeader requestHeader, DynamicModel model)
            throws IOException, SQLException {
        model.addAttribute("memo", memoService.findAll());

        return HttpResponseBuilder.build(
                Links.MAIN,
                DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model),
                HttpResponseHeader.REDIRECT_302_WITH_LOGOUT_COOKIE,
                requestHeader.getAccept()
        );
    }

    private static HttpResponse postUserLogin(RequestHeader requestHeader, DynamicModel model)
            throws SQLException, IOException {
        String userId = requestHeader.getParameter("userId");
        String password = requestHeader.getParameter("password");
        if (userService.login(userId, password)) {
            return buildSuccessLogin(requestHeader, model);
        }
        return buildFailLogin(requestHeader, model);
    }

    private static HttpResponse buildSuccessLogin(RequestHeader requestHeader, DynamicModel model)
            throws IOException, SQLException {
        model.addAttribute("memo", memoService.findAll());

        return HttpResponseBuilder.build(
                Links.MAIN,
                DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model),
                HttpResponseHeader.REDIRECT_302_WITH_LOGIN_COOKIE,
                requestHeader.getAccept()
        );
    }

    private static HttpResponse buildFailLogin(RequestHeader requestHeader, DynamicModel model)
            throws IOException {
        return HttpResponseBuilder.build(
                Links.LOGIN_FAILED,
                DynamicHtmlBuilder.getDynamicHtml(readBody(Links.LOGIN_FAILED), model),
                HttpResponseHeader.REDIRECT_302,
                requestHeader.getAccept()
        );
    }

    private static HttpResponse getUserList(RequestHeader requestHeader, DynamicModel model)
            throws SQLException, IOException {
        if (requestHeader.existCookie("logined") && requestHeader.getCookie("logined").equals("true")) {
            model.addAttribute("users", userService.findAll());
            return logined(requestHeader, model);
        }
        return redirectIndex(requestHeader, model);
    }

    private static HttpResponse logined(RequestHeader requestHeader, DynamicModel model)
            throws IOException {
        return HttpResponseBuilder.build(
                Links.USER_LIST,
                DynamicHtmlBuilder.getDynamicHtml(readBody(Links.USER_LIST), model),
                HttpResponseHeader.RESPONSE_200,
                requestHeader.getAccept()
        );
    }

    private static HttpResponse postUserCreate(RequestHeader requestHeader, DynamicModel model)
            throws SQLException, IOException {
        userService.save(requestHeader);
        return redirectIndex(requestHeader, model);
    }

    private static HttpResponse postMemoWrite(RequestHeader requestHeader, DynamicModel model)
            throws SQLException, IOException {
        memoService.writeMemo(requestHeader);
        model.addAttribute("memo", memoService.findAll());

        return redirectIndex(requestHeader, model);
    }

    private static HttpResponse redirectIndex(RequestHeader requestHeader, DynamicModel model)
            throws IOException, SQLException {

        model.addAttribute("memo", memoService.findAll());

        return HttpResponseBuilder.build(
                Links.MAIN,
                DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model),
                HttpResponseHeader.REDIRECT_302,
                requestHeader.getAccept()
        );
    }

    private static HttpResponse getIndex(RequestHeader requestHeader, DynamicModel model)
            throws SQLException, IOException {
        model.addAttribute("memo", memoService.findAll());

        return HttpResponseBuilder.build(
                Links.MAIN,
                DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model),
                HttpResponseHeader.RESPONSE_200,
                requestHeader.getAccept()
        );
    }

    private static byte[] readBody(String uri) throws IOException {
        return Files.readAllBytes(new File(Links.RETURN_BASE + uri).toPath());
    }
}
