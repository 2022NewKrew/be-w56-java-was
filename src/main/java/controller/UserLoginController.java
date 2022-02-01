package controller;

import db.DataBase;
import exception.BadRequestException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpResponseFactory;
import java.io.DataOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import model.User;

public class UserLoginController implements Controller {

    private static UserLoginController instance;

    public static synchronized UserLoginController getInstance() {
        if (instance == null) {
            instance = new UserLoginController();
        }
        return instance;
    }

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) {
        Map<String, String> bodyData = request.getBodyData();

        checkBodyData(bodyData);

        String userId = bodyData.get("userId");
        String password = bodyData.get("password");
        User user = DataBase.findUserById(userId);

        if (user != null && user.getPassword().equals(password)) {
            return loginSuccess(dos);
        }

        return loginFail(dos);
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

    private HttpResponse loginSuccess(DataOutputStream dos) {
        return HttpResponseFactory.getHttpResponse(
                Map.of("url", "/index.html", "status", "302"),
                Map.of("logined", "true; Path=/"),
                Collections.unmodifiableMap(new HashMap<>()),
                dos);
    }

    private HttpResponse loginFail(DataOutputStream dos) {
        return HttpResponseFactory.getHttpResponse(
                Map.of("url", "/user/login_failed.html", "status", "401"),
                Collections.unmodifiableMap(new HashMap<>()),
                dos);
    }
}
