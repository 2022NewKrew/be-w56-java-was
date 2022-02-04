package framework.util;

import java.util.*;

/**
 * Cookie들을 담을 일급 컬렉션 클래스
 */
public class Cookies {
    private final Map<String, String> cookies = new HashMap<>();

    public String getCookie(String key) {
        return cookies.get(key);
    }

    public void setCookie(String key, String value) {
        cookies.put(key, value);
    }

    public void setCookie(String key, String value, String path) {
        cookies.put(key, value + "; Path=" + path);
    }

    public void parseCookies(String cookiesStr) {
        Arrays.stream(cookiesStr.split(";")).forEach(cookie -> {
            String[] splited = cookie.trim().split("=");
            String key = splited[0];

            String value = "";

            if (splited.length == 2) {
                value = splited[1];
            }

            cookies.put(key, value);
        });
    }

    public boolean isEmpty() {
        return cookies.isEmpty();
    }

    public boolean contains(String key) {
        return cookies.containsKey(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
        }

        sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
