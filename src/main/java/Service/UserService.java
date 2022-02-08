package Service;

import db.DataBase;
import model.User;

import java.util.Map;

public class UserService {

    public static User join(Map<String, String> userMap){
        User user = new User(userMap);
        DataBase.addUser(user);
        return user;
    }

    public static User login(Map<String, String> userMap) throws IllegalArgumentException{
        User user = DataBase.findUserById(userMap.get("userId"));
        if ( user == null){
            throw new IllegalArgumentException("No matching ID !");
        }
        if (! user.validateUser(userMap.get("password"))){
            throw new IllegalArgumentException("No matching PW !");
        }
        return user;

    }
}
