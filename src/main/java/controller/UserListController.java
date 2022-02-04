package controller;

import db.DataBase;
import dto.UserDto;
import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import mapper.UserMapper;
import util.MapUtil;

public class UserListController implements Controller {

    private static UserListController instance;

    public static synchronized UserListController getInstance() {
        if (instance == null) {
            instance = new UserListController();
        }

        return instance;
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

        List<UserDto> userDtos = DataBase.findAll()
                .stream()
                .map(UserMapper.instance::userToDto)
                .collect(Collectors.toList());

        Map<String, Object> model = Map.of("users", userDtos);

        return HttpResponse.ok(
                request.getUrl(),
                model,
                MapUtil.getEmptyCookieMap(),
                dos
        );
    }
}
