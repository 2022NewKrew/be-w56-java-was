package domain;

import java.util.HashMap;
import java.util.Map;

public class UserList {
    private static Map<String, User> userList = new HashMap<>();

    public static void append(String userId, User user){
        userList.put(userId, user);
    }

    public static User get(String userId){
        return userList.get(userId);
    }
}
