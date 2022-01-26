package framework.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Cookies {
    private final Map<String, String> cookies = new HashMap<>();

    public String getCookie(String key) {
        return cookies.get(key);
    }

    public void setCookie(String key, String value) {
        cookies.put(key, value);
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

    public void clear() {
        cookies.clear();
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
