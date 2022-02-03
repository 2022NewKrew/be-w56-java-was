package controller;

import db.DataBase;
import exception.BadRequestException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import java.util.Map;
import model.User;
import util.ControllerUtils;

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

        return HttpResponse.found(
                "/index.html",
                ControllerUtils.getEmptyCookieMap(),
                dos);
    }

    private void checkBodyData(Map<String, String> bodyData) {
        if (bodyData == null || bodyData.isEmpty()) {
            throw new BadRequestException("request body로 전달받은 데이터가 없습니다.");
        }
    }
}
