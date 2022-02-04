package http.response;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import util.Constant;
import util.MapUtil;

public class ResponseHeader {

    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String LOCATION = "Location";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String EXTENSION_SEPARATOR = "\\.";
    public static final String HEADER_LINE_SEPARATOR = ": ";
    public static final String COOKIE_SEPARATOR = "=";

    private final Map<String, String> components;
    private final Map<String, String> cookies;

    public ResponseHeader(Map<String, String> components, Map<String, String> cookies) {
        this.components = components;
        this.cookies = cookies;
    }

    public ResponseHeader() {
        this(MapUtil.newEmptyMap(String.class, String.class),
                MapUtil.newEmptyMap(String.class, String.class));
    }

    public void addContentType(String url) {
        List<String> splitResult = List.of(url.split(EXTENSION_SEPARATOR));
        if (splitResult.size() == 1) {
            addComponent(CONTENT_TYPE, ContentType.DEFAULT.getType());
            return;
        }
        String extension = splitResult.get(splitResult.size() - 1);
        addComponent(CONTENT_TYPE, ContentType.getContentType(extension));
    }

    public void addContentType(ContentType type) {
        addComponent(CONTENT_TYPE, type.getType());
    }

    public void addContentLength(int length) {
        addComponent(CONTENT_LENGTH, String.valueOf(length));
    }

    public void addLocation(String location) {
        addComponent(LOCATION, location);
    }

    public void addCookie(Map<String, String> cookie) {
        if (cookie.isEmpty()) {
            return;
        }
        this.cookies.putAll(cookie);
    }

    private void addComponent(String key, String value) {
        components.put(key, value);
    }

    public String getComponentString() {
        StringBuilder result = new StringBuilder();

        for (Entry<String, String> entry : components.entrySet()) {
            result.append(entry.getKey()).append(HEADER_LINE_SEPARATOR).append(entry.getValue())
                    .append(Constant.lineBreak);
        }

        for (Entry<String, String> entry : cookies.entrySet()) {
            result
                    .append(SET_COOKIE)
                    .append(HEADER_LINE_SEPARATOR)
                    .append(entry.getKey())
                    .append(COOKIE_SEPARATOR)
                    .append(entry.getValue())
                    .append(Constant.lineBreak);
        }

        return result.toString();
    }
}
