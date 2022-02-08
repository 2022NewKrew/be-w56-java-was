package http;

import com.google.common.base.Strings;
import util.ParsingUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Cookie {
    private static final String END_DELIMITER = "; ";
    private static final String FIELD_DELIMITER = "=";
    private final Map<String, String> cookies;

    private Cookie(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public static Cookie parse(String cookies) {
        if (Strings.isNullOrEmpty(cookies)) {
            return new Cookie(new HashMap<>());
        }
        return new Cookie(ParsingUtils.parse(cookies, END_DELIMITER, FIELD_DELIMITER));
    }

    public void setCookie(String key, Object value) {
        // TODO else 없애기
        if (cookies.containsKey(key)) {
            cookies.replace(key, value.toString());
        } else {
            cookies.put(key, value.toString());
        }
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }

    public String createCookieHeader() {
        return cookies.entrySet()
                .stream()
                .map(cookie -> cookie.getKey() + FIELD_DELIMITER + cookie.getValue())
                .collect(Collectors.joining(END_DELIMITER));
    }

    public Map<String, String> getCookie() {
        return cookies;
    }
}
