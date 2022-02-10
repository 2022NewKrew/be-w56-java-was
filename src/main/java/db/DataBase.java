package db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import model.Memo;
import model.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();
    private static Map<Integer, Memo> memos = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserByUserId(String userId) {
        return users.get(userId);
    }

    public static List<User> findAllUser() {
        return new ArrayList<>(users.values());
    }

    public static void addMemo(Memo memo) {
        memos.put(memo.getId(), memo);
    }

    public static List<Memo> findAllMemo() {
        return new ArrayList<>(memos.values());
    }
}
