package controller;

import dao.UserDao;
import exception.BadRequestException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import java.util.Map;
import model.User;
import util.MapUtil;

public class UserLoginController implements Controller {

    private static final UserLoginController INSTANCE = new UserLoginController();
    private final UserDao userDao = UserDao.getInstance();

    public static synchronized UserLoginController getInstance() {
        return INSTANCE;
    }

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) {
        Map<String, String> bodyData = request.getBodyData();

        checkBodyData(bodyData);

        String userId = bodyData.get("userId");
        String password = bodyData.get("password");
        User user = userDao.findByUserId(userId);

        if (user != null && user.getPassword().equals(password)) {
            return loginSuccess(dos, userId);
        }

        return loginFail(dos);
    }

    private void checkBodyData(Map<String, String> bodyData) {
        if (bodyData == null || bodyData.isEmpty()) {
            throw new BadRequestException("request body로 전달받은 데이터가 없습니다.");
        }
    }

    private HttpResponse loginSuccess(DataOutputStream dos, String userId) {
        return HttpResponse.found(
                "/index.html",
                Map.of("logined", "true; Path=/", "userId", userId + "; Path=/"),
                dos);
    }

    private HttpResponse loginFail(DataOutputStream dos) {
        return HttpResponse.unauthorized(
                "/user/login_failed.html",
                MapUtil.getEmptyModelMap(),
                dos);
    }
}
