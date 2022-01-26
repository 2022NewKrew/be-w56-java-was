package framework.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestAttributes {
    private final Map<String, String> requestAttributes = new HashMap<>();

    public String getAttribute(String key) {
        return requestAttributes.get(key);
    }

    public void setAttributes(String key, String value) {
        requestAttributes.put(key, value);
    }

    public void parseAttributes(String attributesStr) {
        Arrays.stream(attributesStr.split("&")).forEach(attribute -> {
            String[] splited = attribute.split("=");
            String key = splited[0];

            String value = "";

            if (splited.length == 2) {
                value = splited[1];
            }

            requestAttributes.put(key, value);
        });
    }

    public void clear() {
        requestAttributes.clear();
    }
}
