package service;

import db.DataBase;
import model.User;
import webserver.http.request.InfoMap;

import java.util.Map;

public class UserService {
    private final DataBase dataBase;

    public UserService() {
        dataBase = DataBase.getInstance();
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
