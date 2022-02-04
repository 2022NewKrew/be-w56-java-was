package http.request;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Constant;

public class RequestHeader {

    private final Map<String, String> components;
    private final Map<String, String> cookies;

    public RequestHeader(Map<String, String> components, Map<String, String> cookies) {
        this.components = Collections.unmodifiableMap(components);
        this.cookies = Collections.unmodifiableMap(cookies);
    }

    public static RequestHeader stringToRequestHeader(String headers) {
        if (headers.isEmpty()) {
            return new RequestHeader(Collections.emptyMap(), Collections.emptyMap());
        }
        List<String> headerLines = List.of(headers.split(Constant.lineBreak));
        Map<String, String> components = getComponents(headerLines);
        Map<String, String> cookies = getCookies(findCookieLine(headerLines));
        return new RequestHeader(components, cookies);
    }

    private static Map<String, String> getComponents(List<String> headerLines) {
        Map<String, String> result = new HashMap<>();
        List<String> splitLine;
        for (String headerLine : headerLines) {
            if (headerLine.contains("Cookie")) {
                continue;
            }
            splitLine = List.of(headerLine.split(": "));
            result.put(splitLine.get(0), splitLine.get(1));
        }
        return result;
    }

    private static String findCookieLine(List<String> headerLines) {
        for (String headerLine : headerLines) {
            if (headerLine.contains("Cookie")) {
                return headerLine;
            }
        }
        return "";
    }

    private static Map<String, String> getCookies(String cookieLine) {
        Map<String, String> result = new HashMap<>();

        if (cookieLine.isEmpty()) {
            return result;
        }

        List<String> cookieStrings = List.of(cookieLine.split(": ")[1].trim().split(";"));

        List<String> entry;
        String key;
        String value;
        for (String cookieString : cookieStrings) {
            entry = List.of(cookieString.trim().split("="));
            key = entry.get(0);
            value = entry.get(1);
            result.put(key, value);
        }

        return result;
    }

    public boolean hasComponent(String key) {
        return components.containsKey(key);
    }

    public String getComponent(String key) {
        return components.get(key);
    }

    public boolean hasCookie(String key) {
        return cookies.containsKey(key);
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }
}
