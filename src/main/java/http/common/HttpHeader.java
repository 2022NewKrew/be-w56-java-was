package http.common;

import com.google.common.net.HttpHeaders;
import http.response.Cookie;

public class HttpHeader {
    private static final String HTTP_HEADER_LINE_DELIMITER = "\r\n";
    private static final String HTTP_HEADER_KEY_VALUE_DELIMITER = ": ";

    private final String name;
    private final String value;

    public HttpHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static HttpHeader fromCookie(Cookie cookie) {
        return new HttpHeader(HttpHeaders.SET_COOKIE, cookie.toHeaderString());
    }

    public boolean isNameMatched(String name) {
        return this.name.equals(name);
    }

    public String getValue() {
        return value;
    }

    public String getFormattedHeader() {
        return String.format("%s%s%s%s", name, HTTP_HEADER_KEY_VALUE_DELIMITER, value, HTTP_HEADER_LINE_DELIMITER);
    }

    @Override
    public String toString() {
        return "Header{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
