package service;

import db.DataBase;
import dto.UserCreateDto;
import dto.UserLoginDto;
import dto.UserProfileDto;
import http.request.HttpRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.User;

public class UserService {

    private static final String LOGINED = "logined";
    private static UserService instance;

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void register(UserCreateDto userCreateDto) {
        DataBase.addUser(userCreateDto.toEntity());
    }

    public User login(UserLoginDto userLoginDto) {
        User result = DataBase.findUserById(userLoginDto.getUserId());
        if (result == null || !result.getPassword().equals(userLoginDto.getPassword())) {
            return null;
        }

        return result;
    }

    public boolean isLogin(HttpRequest request) {
        Map<String, String> cookies = request.getHeaders().getCookie();
        return cookies.containsKey(LOGINED) && cookies.get(LOGINED).equals("true");
    }

    public List<UserProfileDto> getAllUsers() {
        Collection<User> users = DataBase.findAll();
        return users.stream()
                .map(UserProfileDto::from)
                .collect(Collectors.toList());
    }
}
