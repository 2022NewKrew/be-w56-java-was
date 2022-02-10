package controller;

import db.DataBase;
import exceptions.InvalidRequestFormatException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.HttpClientErrorResponse;
import model.HttpMethod;
import model.HttpRedirectionResponse;
import model.HttpRequest;
import model.HttpResponse;
import model.HttpStatus;
import model.HttpSuccessfulResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.View;

public class UserCreateController implements Controller {

    private static final String PAIR_SPLIT_DELIMITER = "&";
    private static final String KEY_VALUE_SPLIT_DELIMITER = "=";
    private static final String EMPTY_COOKIE = "";
    private static final int KEY_VALUE_SPLIT_RESULT_SIZE = 2;
    private static UserCreateController instance;
    private static final Logger log = LoggerFactory.getLogger(UserCreateController.class);


    public static synchronized UserCreateController getInstance() {
        if (instance == null) {
            instance = new UserCreateController();
        }
        return instance;
    }

    private Map<String, String> getUserElement(String body) {
        String[] splitUserBodyString = body.split(PAIR_SPLIT_DELIMITER);
        Map<String, String> userElementMap = new HashMap<>();
        for (String pair : splitUserBodyString) {
            String[] split = pair.split(KEY_VALUE_SPLIT_DELIMITER);
            if (split.length != KEY_VALUE_SPLIT_RESULT_SIZE) {
                throw new InvalidRequestFormatException("user의 인자가 부족합니다");
            }
            userElementMap.put(
                    URLDecoder.decode(split[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(split[1], StandardCharsets.UTF_8));
        }
        return userElementMap;
    }

    private void saveUser(String body) {
        Map<String, String> userElementMap = getUserElement(body);
        User user = new User(userElementMap.get("userId"), userElementMap.get("password"), userElementMap.get("name"),
                userElementMap.get("email"));
        log.debug("User 생성 {}", user);
        DataBase.addUser(user);
        log.debug("User DB 저장 {}", user);
    }

    private List<User> getUserList() {
        log.debug("User 목록 조회");
        List<User> userList = new ArrayList<>(DataBase.findAll());
        log.debug("User 목록 조회 완료 {}", userList);
        return userList;
    }

    @Override
    public HttpResponse run(HttpRequest request) throws IOException {
        if (request.getHttpMethod() == HttpMethod.POST) {
            saveUser(request.getBody());
            return HttpRedirectionResponse.of(HttpStatus.FOUND, request.getUrl(), EMPTY_COOKIE);
        } else if (request.getHttpMethod() == HttpMethod.GET) {
            List<User> userList = getUserList();
            return HttpSuccessfulResponse.of(HttpStatus.OK, request.getUrl(), View.userList(userList));
        }
        return HttpClientErrorResponse.of(HttpStatus.NOT_FOUND, View.staticFile("/errors/notFound.html"));
    }
}
