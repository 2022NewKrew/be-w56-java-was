package http.response;

import java.util.HashMap;
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

    public void addComponent(String key, String value) {
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
