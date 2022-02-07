package domain.service;

import domain.db.DataBase;
import domain.model.User;

import java.util.Map;

public class LoginService {
    public void join(Map<String, String> params) {
        User user = new User(params.get("userId"),params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(user);
    }

    public boolean login(Map<String, String> params) {
        User user = DataBase.findUserById(params.get("userId"));
        if(user == null) {
            return false;
        }
        if(!user.getPassword().equals(params.get("password"))) {
            return false;
        }
        return true;
    }
}
