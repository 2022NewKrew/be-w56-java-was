package http.response;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import util.Constant;
import util.MapUtil;

public class ResponseHeader {

    private final Map<String, String> components;
    private final Map<String, String> cookies;

    public ResponseHeader(Map<String, String> components, Map<String, String> cookies) {
        this.components = components;
        this.cookies = cookies;
    }

    public ResponseHeader() {
        this(MapUtil.get(String.class, String.class), MapUtil.get(String.class, String.class));
    }

    public void addContentType(String url) {
        List<String> splitResult = List.of(url.split("\\."));
        if (splitResult.size() == 1) {
            addComponent("Content-Type", ContentType.DEFAULT.getType());
            return;
        }
        String extension = splitResult.get(splitResult.size() - 1);
        addComponent("Content-Type", ContentType.getContentType(extension));
    }

    public void addContentType(ContentType type) {
        addComponent("Content-Type", type.getType());
    }

    public void addContentLength(int length) {
        addComponent("Content-Length", String.valueOf(length));
    }

    public void addLocation(String location) {
        addComponent("Location", location);
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
            result.append(entry.getKey()).append(": ").append(entry.getValue())
                    .append(Constant.lineBreak);
        }

        for (Entry<String, String> entry : cookies.entrySet()) {
            result.append("Set-Cookie").append(": ").append(entry.getKey()).append("=")
                    .append(entry.getValue()).append(Constant.lineBreak);
        }

        return result.toString();
    }
}
