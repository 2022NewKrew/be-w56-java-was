package application.service;

import static application.service.UserServiceConstants.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;

import application.repository.DbUserRepository;
import application.repository.MemoryUserRepository;
import application.model.User;
import application.repository.UserRepository;
import http.request.HttpRequest;
import infrastructure.dto.AppResponse;
import http.common.Status;

public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

//    private final UserRepository userRepository = MemoryUserRepository.getInstance();
    private final UserRepository userRepository = DbUserRepository.getInstance();

    public AppResponse getCreate(HttpRequest httpRequest) {
        String userId = httpRequest.getQueryStringParams(ATTRIBUTE_USER_ID);
        String password = httpRequest.getQueryStringParams(ATTRIBUTE_PASSWORD);
        String name = httpRequest.getQueryStringParams(ATTRIBUTE_NAME);
        String email = httpRequest.getQueryStringParams(ATTRIBUTE_EMAIL);

        return commonCreate(userId, password, name, email);
    }


    private AppResponse commonCreate(String userId, String password, String name, String email) {

        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(name) || StringUtils.isEmpty(email)) {
            return AppResponse.of(SIGNUP_FAIL_FILE, Status.OK);
        }
        userRepository.addUser(new User(userId, password, name, email));

        return AppResponse.of(REDIRECT_PATH, Status.FOUND);
    }

    public AppResponse postCreate(HttpRequest httpRequest) {
        String userId = httpRequest.getBodyParams(ATTRIBUTE_USER_ID);
        String password = httpRequest.getBodyParams(ATTRIBUTE_PASSWORD);
        String name = httpRequest.getBodyParams(ATTRIBUTE_NAME);
        String email = httpRequest.getBodyParams(ATTRIBUTE_EMAIL);

        return commonCreate(userId, password, name, email);
    }

    public AppResponse postLogin(HttpRequest httpRequest) {
        String userId = httpRequest.getBodyParams(ATTRIBUTE_USER_ID);
        String password = httpRequest.getBodyParams(ATTRIBUTE_PASSWORD);

        User findUser = userRepository.findUserById(userId);

        if (findUser != null && findUser.getPassword().equals(password)) {
            AppResponse httpResponse = AppResponse.of(REDIRECT_PATH, Status.FOUND);
            httpResponse.addHeaderAttribute("Set-Cookie", COOKIE_LOGIN + "=true; Path=/");
            return httpResponse;
        }

        AppResponse appResponse = AppResponse.of(LOGIN_FAIL_FILE, Status.FOUND);
        appResponse.addHeaderAttribute("Set-Cookie", COOKIE_LOGIN + "=false; Path=/");
        return appResponse;
    }

    public AppResponse getUserList(HttpRequest httpRequest) {

        List<String> cookie = httpRequest.getHeader("Cookie");
        if (cookie == null || !cookie.contains(COOKIE_LOGIN + "=true")) {
            return AppResponse.of(LOGIN_PAGE, Status.OK);
        }

        List<User> findUsers = userRepository.findAll();
        String html = userListTemplate(findUsers);
        AppResponse appResponse = AppResponse.of(USER_LIST, Status.OK);
        appResponse.addModelAttribute("userList", html);

        return appResponse;
    }

    private String userListTemplate(List<User> users) {
        StringBuilder sb = new StringBuilder();
        for (int i = users.size() - 1; i >= 0; i--) {
            sb.append(userListTemplateEntry(users.get(i), i + 1));
        }
        return sb.toString();
    }

    private String userListTemplateEntry(User user, int num) {
        return String.format(USER_LIST_ENTRY, num, user.getUserId(), user.getName(), user.getEmail());
    }
}
