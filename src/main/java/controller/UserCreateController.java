package controller;

import db.DataBase;
import exceptions.BadRequestFormatException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import model.HttpRedirectionResponse;
import model.HttpRequest;
import model.HttpResponse;
import model.HttpStatus;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserCreateController implements Controller {

    private static final String PAIR_SPLIT_DELIMITER = "&";
    private static final String KEY_VALUE_SPLIT_DELIMITER = "=";
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
                throw new BadRequestFormatException("user의 인자가 부족합니다");
            }
            userElementMap.put(
                    URLDecoder.decode(split[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(split[1], StandardCharsets.UTF_8));
        }
        return userElementMap;
    }

    @Override
    public HttpResponse run(HttpRequest request) {
        Map<String, String> userElementMap = getUserElement(request.getBody());
        User user = new User(userElementMap.get("userId"), userElementMap.get("password"), userElementMap.get("name"),
                userElementMap.get("email"));
        log.debug("User 생성 {}", user);
        DataBase.addUser(user);
        log.debug("User DB 저장 {}", user);
        return HttpRedirectionResponse.of(HttpStatus.FOUND, request.getUrl());
    }
}
