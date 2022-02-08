package Service;

import db.DataBase;
import db.SessionDb;
import model.User;
import webserver.Session;

import java.util.Map;

public class UserService {

    public static User join(Map<String, String> userMap){
        User user = new User(userMap);
        DataBase.addUser(user);
        return user;
    }

    public static String login(Map<String, String> userMap) throws IllegalArgumentException{
        String uid = userMap.get("userId");
        User user = DataBase.findUserById(uid);
        if ( user == null){
            throw new IllegalArgumentException("No matching ID !");
        }
        if (! user.validateUser(userMap.get("password"))){
            throw new IllegalArgumentException("No matching PW !");
        }
        String sessionId = Session.create(uid);
        return sessionId;

    }

    public static String logout(){
        return SessionDb.removeSession();
    }
}
