package controller;

import dao.UserDao;
import dto.UserDto;
import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import java.util.List;
import java.util.Map;
import mapper.UserMapper;
import util.MapUtil;

public class UserListController implements Controller {

    private static final UserListController INSTANCE = new UserListController();
    private static final UserDao userDao = UserDao.getInstance();
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    public static synchronized UserListController getInstance() {
        return INSTANCE;
    }

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) {
        String logined = request.getCookie("logined");
        if (!Boolean.parseBoolean(logined)) {
            return HttpResponse.found(
                    "/user/login.html",
                    MapUtil.getEmptyCookieMap(),
                    dos
            );
        }

        List<UserDto> userDtos = userMapper.usersToDtos(userDao.find());

        Map<String, Object> model = Map.of("users", userDtos);

        return HttpResponse.ok(
                request.getUrl(),
                model,
                MapUtil.getEmptyCookieMap(),
                dos
        );
    }
}
