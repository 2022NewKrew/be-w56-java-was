package util;

import org.checkerframework.checker.units.qual.A;
import util.exception.UserCookieEmptyException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class MySession {

    private static Map<String, Attributes> info = new LinkedHashMap<>();
    private final Attributes attributes;
    private final String sessionUUID;

    private MySession(Attributes attributes, String sessionUUID) {
        this.attributes = attributes;
        this.sessionUUID = sessionUUID;
    }

    public static MySession newInstance() {
        String sessionUUID = String.valueOf(UUID.randomUUID());
        Attributes attributes = new Attributes();
        info.put(sessionUUID, attributes);
        return new MySession(attributes, sessionUUID);
    }

    public static MySession getInstance(String sessionUUID) {
        if (info.containsKey(sessionUUID)) {
            return new MySession(info.get(sessionUUID), sessionUUID);
        }

        throw new UserCookieEmptyException("접속 USER의 Cookie가 유효하지 않습니다.");

    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public String getSessionUUID() {
        return sessionUUID;
    }

    private static class Attributes {
        final Map<String, Object> info = new HashMap<>();

        void put(String key, Object value) {
            info.put(key, value);
        }

        Object get(String key) {
            return info.get(key);
        }
    }
}
