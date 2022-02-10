package db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import model.Model;
import model.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();

    static {
        init();
    }

    public static List<Model> getUserList(){
        return new ArrayList<>(users.values());
    }

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.getOrDefault(userId, null);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    // todo : 실제 사용시 아래 함수 제거 (test 에서만 사용)
    public static void init(){
        User user1 = new User("id1","pw1","name1","e1@kakao.com");
        User user2 = new User("id2","pw2","name2","e3@kakao.com");
        addUser(user1);
        addUser(user2);
    }

    public static String printUserIdPw() {
        final StringBuffer sb = new StringBuffer("DataBase{");
        for(String uid : users.keySet()){
            String pw = users.get(uid).getPassword();
            sb.append(uid + " - "+ pw + " / ");
        }
        sb.append('}');
        return sb.toString();
    }
}
