package controller;

import db.DataBase;
import exception.BadRequestException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import java.util.Map;
import model.User;
import util.ControllerUtils;

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
            return loginSuccess(request, dos);
        }

        return loginFail(request, dos);
    }

    private void checkBodyData(Map<String, String> bodyData) {
        if (bodyData == null || bodyData.isEmpty()) {
            throw new BadRequestException("request body로 전달받은 데이터가 없습니다.");
        }
    }

    private HttpResponse loginSuccess(HttpRequest request, DataOutputStream dos) {
        return HttpResponse.found(
                "/index.html",
                Map.of("logined", "true; Path=/"),
                dos);
    }

    private HttpResponse loginFail(HttpRequest request, DataOutputStream dos) {
        return HttpResponse.unauthorized(
                request.getUrl(),
                ControllerUtils.getEmptyImmutableMap(),
                dos);
    }
}
