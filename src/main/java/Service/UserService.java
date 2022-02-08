package Service;

import model.User;

import java.util.Map;

import static db.DataBase.addUser;

public class UserService {

    public static User join(Map<String, String> userMap){
        User user = new User(userMap);
        addUser(user);
        return user;
    }
}
