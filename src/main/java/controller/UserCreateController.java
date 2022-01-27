package controller;

import db.DataBase;
import exception.BadRequestException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpResponseFactory;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;
import model.User;

public class UserCreateController implements Controller {

    private static UserCreateController instance;

    public static synchronized UserCreateController getInstance() {
        if (instance == null) {
            instance = new UserCreateController();
        }
        return instance;
    }

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) {
        Map<String, String> bodyData = request.getBodyData();

        checkBodyData(bodyData);

        DataBase.addUser(
                new User(bodyData.get("userId"), bodyData.get("password"), bodyData.get("name"),
                        bodyData.get("email")));

        Map<String, String> result = new HashMap<>();
        result.put("url", "/index.html");
        result.put("status", "302");

        return HttpResponseFactory.getHttpResponse(result, new HashMap<>(), dos);
    }

    private void checkBodyData(Map<String, String> bodyData) {
        if (bodyData == null || bodyData.isEmpty()) {
            throw new BadRequestException("request body로 전달받은 데이터가 없습니다.");
        }
        for (String value : bodyData.values()) {
            if (value == null) {
                throw new BadRequestException("request body에 null 값이 포함되어 있습니다.");
            }
        }
    }
}
