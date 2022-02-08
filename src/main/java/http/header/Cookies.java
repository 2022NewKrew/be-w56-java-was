package http.header;

import com.google.common.base.Strings;
import util.ParsingUtils;

import java.util.HashMap;
import java.util.Map;

public class Cookies {
    private static final String END_DELIMITER = "; ";
    private static final String VALUE_DELIMITER = "=";
    private final Map<String, String> cookies;

    private Cookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public static Cookies parse(String cookies) {
        if (Strings.isNullOrEmpty(cookies)) {
            return new Cookies(new HashMap<>());
        }

        return new Cookies(ParsingUtils.parse(cookies, END_DELIMITER, VALUE_DELIMITER));
    }

    public Cookie getCookie(String key) {
        return new Cookie(key, cookies.get(key));
    }
}
