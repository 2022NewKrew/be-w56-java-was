package controller;

import db.DataBase;
import exceptions.InvalidRequestFormatException;
import exceptions.LoginFailedException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import model.HttpClientErrorResponse;
import model.HttpMethod;
import model.HttpRedirectionResponse;
import model.HttpRequest;
import model.HttpResponse;
import model.HttpStatus;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.View;

public class LoginController implements Controller {

    private static final String PAIR_SPLIT_DELIMITER = "&";
    private static final String KEY_VALUE_SPLIT_DELIMITER = "=";
    private static final int KEY_VALUE_SPLIT_RESULT_SIZE = 2;
    private static final String LOGIN_COOKIE = "logined=true";
    private static LoginController instance;
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    public static synchronized LoginController getInstance() {
        if (instance == null) {
            instance = new LoginController();
        }
        return instance;
    }

    private Map<String, String> getLoginElement(String body) {
        String[] splitUserBodyString = body.split(PAIR_SPLIT_DELIMITER);
        Map<String, String> loginElementMap = new HashMap<>();
        for (String pair : splitUserBodyString) {
            String[] split = pair.split(KEY_VALUE_SPLIT_DELIMITER);
            if (split.length != KEY_VALUE_SPLIT_RESULT_SIZE) {
                throw new InvalidRequestFormatException("login 인자가 부족합니다");
            }
            loginElementMap.put(
                    URLDecoder.decode(split[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(split[1], StandardCharsets.UTF_8));
        }
        return loginElementMap;
    }

    public void validLogin(String body) {
        Map<String, String> userElementMap = getLoginElement(body);
        String userId = userElementMap.get("userId");
        String password = userElementMap.get("password");

        if (Objects.isNull(userId) || Objects.isNull(password)) {
            throw new InvalidRequestFormatException(String.format("login시 인자가 부족합니다 %s %s", userId, password));
        }
        User user = DataBase.findUserById(userId);
        if (Objects.isNull(user)) {
            throw new LoginFailedException("해당 유저가 없습니다");
        }
        if (!Objects.equals(user.getUserId(), userId) || !Objects.equals(user.getPassword(), password)) {
            throw new LoginFailedException("유저 정보가 일치하지 않습니다");
        }
        log.debug("로그인 성공 {} {}", userId, password);
    }

    @Override
    public HttpResponse run(HttpRequest request) throws IOException {
        if (request.getHttpMethod() == HttpMethod.POST) {
            validLogin(request.getBody());
            return HttpRedirectionResponse.of(HttpStatus.FOUND, "/post/list.html", LOGIN_COOKIE);
        }
        return HttpClientErrorResponse.of(HttpStatus.NOT_FOUND, View.staticFile("/errors/notFound.html"));
    }
}
