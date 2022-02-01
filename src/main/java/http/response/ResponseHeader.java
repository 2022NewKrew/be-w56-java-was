package http.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import util.Constant;

public class ResponseHeader {

    private final Map<String, String> components;

    public ResponseHeader(Map<String, String> components) {
        this.components = components;
    }

    public ResponseHeader() {
        this(new HashMap<>());
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
        for (Entry<String, String> entry : cookie.entrySet()) {
            addComponent("Set-Cookie", entry.getKey() + "=" + entry.getValue());
        }
    }

    private void addComponent(String key, String value) {
        components.put(key, value);
    }

    public String getComponentString() {
        StringBuilder result = new StringBuilder();

        for (Entry<String, String> entry : components.entrySet()) {
            result.append(entry.getKey() + ": " + entry.getValue() + Constant.lineBreak);
        }

        return result.toString();
    }
}
