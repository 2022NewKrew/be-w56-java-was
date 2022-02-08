package http.header;

import util.ParsingUtils;

public class Cookie {
    private static final String END_DELIMITER = "; ";
    private static final String VALUE_DELIMITER = "=";
    private final String cookieName;
    private final String cookieValue;
    private String path;

    public Cookie(String cookieName, String cookieValue) {
        this.cookieName = cookieName;
        this.cookieValue = cookieValue;
        this.path = null;
    }

    public static Cookie parse(String cookie) {
        String[] tokens = ParsingUtils.parse(cookie, VALUE_DELIMITER);
        return new Cookie(tokens[0], tokens[1]);
    }

    public String createHeader() {
        if (path != null) {
            return cookieName + VALUE_DELIMITER + cookieValue + END_DELIMITER + "Path=" + path;
        }
        return cookieName + VALUE_DELIMITER + cookieValue;
    }

    public String getName() {
        return cookieName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getValue() {
        return cookieValue;
    }
}
