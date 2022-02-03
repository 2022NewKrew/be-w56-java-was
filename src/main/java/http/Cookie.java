package http;

import com.google.common.base.Strings;
import util.Pair;
import util.ParsingUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
        return new Cookie(Arrays.stream(ParsingUtils.parse(cookies, END_DELIMITER))
                .map(cookie -> ParsingUtils.getKeyValue(cookie, FIELD_DELIMITER))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue)));
    }

    public void setCookie(String key, Object value) {
        cookies.put(key, value.toString());
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
