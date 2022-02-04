package model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class UserAccountHtmlMapper {
    private final Map<String, Supplier<String>> map = new HashMap<>();

    public UserAccountHtmlMapper(UserAccount userAccount) {
        map.put("id", userAccount::getId);
        map.put("userId", userAccount::getUserId);
        map.put("name", userAccount::getName);
        map.put("email", userAccount::getEmail);
    }

    public Map<String, Supplier<String>> getMap() {
        return map;
    }
}
