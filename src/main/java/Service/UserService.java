package Service;

import DTO.RequestHeader;
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

        return Session.create(uid);

    }

    public static String logout(){
        return SessionDb.removeSession();
    }

    public static boolean checkLogin(RequestHeader requestHeader){ // true: logined
        return (SessionDb.isExist() && requestHeader.isLogin());
    }
}
