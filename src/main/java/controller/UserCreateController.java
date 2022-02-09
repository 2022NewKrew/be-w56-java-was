package controller;

import dao.UserDao;
import exception.BadRequestException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import java.util.Map;
import model.User;
import util.MapUtil;

public class UserCreateController implements Controller {

    private static final UserCreateController INSTANCE = new UserCreateController();
    private final UserDao userDao = UserDao.getInstance();

    public static synchronized UserCreateController getInstance() {
        return INSTANCE;
    }

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) {
        Map<String, String> bodyData = request.getBodyData();

        checkBodyData(bodyData);

        User user = new User(bodyData.get("userId"), bodyData.get("password"), bodyData.get("name"),
                bodyData.get("email"));

        if (userDao.findByUserId(user.getUserId()) != null) {
            throw new BadRequestException();
        }

        userDao.save(user);

        return HttpResponse.found(
                "/index.html",
                MapUtil.getEmptyCookieMap(),
                dos);
    }

    private void checkBodyData(Map<String, String> bodyData) {
        if (bodyData == null || bodyData.isEmpty()) {
            throw new BadRequestException("request body로 전달받은 데이터가 없습니다.");
        }
    }
}
