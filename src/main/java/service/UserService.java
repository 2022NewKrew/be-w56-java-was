package service;

import db.DataBase;
import model.User;
import webserver.http.request.InfoMap;

public class UserService {
    private final DataBase dataBase;
    private static final UserService userService = new UserService();

    private UserService() {
        dataBase = DataBase.getInstance();
    }

    public static UserService getInstance() {
        return userService;
    }

    public void createUser(InfoMap userInfoMap) {
        dataBase.addUser(getUserFromUserDataMap(userInfoMap));
    }

    private User getUserFromUserDataMap(InfoMap userInfoMap) {
        String userId = userInfoMap.get("userId");
        String password = userInfoMap.get("password");
        String name = userInfoMap.get("name");
        String email = userInfoMap.get("email");

        return new User(userId, password, name, email);
    }
}
