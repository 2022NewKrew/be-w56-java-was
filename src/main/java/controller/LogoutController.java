package controller;

import exceptions.InvalidRequestFormatException;
import exceptions.LogoutFailedException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import model.Cookie;
import model.HttpClientErrorResponse;
import model.HttpHeader;
import model.HttpMethod;
import model.HttpRedirectionResponse;
import model.HttpRequest;
import model.HttpResponse;
import model.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.View;

public class LogoutController implements Controller {

    private static LogoutController instance;
    private static final int KEY_VALUE_SPLIT_RESULT_SIZE = 2;
    private static final Logger log = LoggerFactory.getLogger(LogoutController.class);

    public static synchronized LogoutController getInstance() {
        if (instance == null) {
            instance = new LogoutController();
        }
        return instance;
    }

    private boolean isCookieHeaderExist(String cookieHeader) {
        return Objects.nonNull(cookieHeader);
    }

    private List<String> parseCookieHeader(String cookieHeader) {
        List<String> parseString = List.of(cookieHeader.split("="));
        if (parseString.size() != KEY_VALUE_SPLIT_RESULT_SIZE) {
            throw new InvalidRequestFormatException("쿠키 형식이 잘못되었습니다 key-value");
        }
        return parseString;
    }

    private boolean isLogined(List<String> parseString) {
        return Objects.equals(parseString.get(0), "logined") && Objects.equals(parseString.get(1), "true");
    }

    private void validLogout(String cookieHeader) {
        if (!isCookieHeaderExist(cookieHeader)) {
            throw new LogoutFailedException("이미 로그아웃 상태입니다");
        }
        List<String> parseString = parseCookieHeader(cookieHeader);
        if (isLogined(parseString)) {
            return;
        }
        throw new LogoutFailedException("이미 로그아웃 상태입니다");
    }

    @Override
    public HttpResponse run(HttpRequest request) throws IOException {
        if (request.getHttpMethod() == HttpMethod.GET) {
            validLogout(request.headerValue(HttpHeader.COOKIE));
            return HttpRedirectionResponse.of(HttpStatus.FOUND, "/post/list.html", Cookie.makeByLoginSuccess(false));
        }
        return HttpClientErrorResponse.of(HttpStatus.NOT_FOUND, View.staticFile("/errors/notFound.html"));
    }
}
