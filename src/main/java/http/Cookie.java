package http;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Cookie {
    private final String FIELD_DELIMITER = "=";
    private final String END_DELIMITER = ";";
    private final String EMPTY_STRING = " ";
    private final Map<String, String> cookies;

    public Cookie() {
        cookies = new LinkedHashMap<>();
    }

    public void setCookie(String key, Object value) {
        cookies.put(key, value.toString());
    }

    public String createCookieHeader() {
        String header = cookies.entrySet()
                .stream()
                .map(cookie -> cookie.getKey() + FIELD_DELIMITER + cookie.getValue())
                .collect(Collectors.joining(END_DELIMITER + EMPTY_STRING));

        if (header.length() == 0)
            return header;

        return header + END_DELIMITER;
    }
}
