package service;

import http.Request;
import http.RequestHeader;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.InMemoryUserRepository;

import java.util.Objects;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static final InMemoryUserRepository userRepository = InMemoryUserRepository.getInstance();

    public static void join(User user) {
        userRepository.addUser(user);
        log.debug("User DataBase Status: {}", userRepository.findAll());
    }

    public static boolean login(String userId, String password) {
        log.debug("User DataBase Status: {}", userRepository.findAll());
        log.debug("userId: {}, password: {}", userId, password);
        User user = userRepository.findUserById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        return Objects.equals(user.getPassword(), password);
    }

    public static boolean checkLogin(Request request) {
        RequestHeader requestHeader = request.getRequestHeader();
        String cookie = requestHeader.getHeaders().get("Cookie");
        log.debug("Cookie: {}", cookie);

        return cookie.contains("logined=true");
    }
}
