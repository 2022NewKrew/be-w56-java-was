package controller;

import model.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import service.UserService;
import util.JsonParseUtils;
import webserver.request.RequestBody;

public class UserController {
//    스프링처럼 이렇게 자동 주입 해주는것처럼 하려면 어떻게 할지 몰라서 일단 static으로 처리해뒀습니다,, 후에 수정하겠습니다
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }

    public static void create(RequestBody requestBody) throws ParseException {
        String body = requestBody.getBody();

        JSONObject jsonObject = JsonParseUtils.stringToJson(body);
        User user = new User((String) jsonObject.get("id"),
                (String) jsonObject.get("password"),
                (String) jsonObject.get("name"),
                (String) jsonObject.get("email"));

        UserService.create(user);
    }
}
