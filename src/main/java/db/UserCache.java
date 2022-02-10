package db;

import model.User;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class UserCache {

    private static final Map<Long, User> cache = new ConcurrentHashMap<>();
    private static final Random random = new Random();

    public static Long addSessionUser(User user) {
        Long sessionId = generateRandomSessionId();
        cache.put(sessionId, user);
        return sessionId;
    }

    public static User getSessionUser(Long sessionId) {
        return cache.get(sessionId);
    }

    // ------------------------------------------------------------

    private static Long generateRandomSessionId() {
        Long sessionId;
        do {
            sessionId = random.nextLong();
        } while (cache.containsKey(sessionId));

        return sessionId;
    }
}
