package http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Cookie {

    private Map<String, String> attributes = new HashMap<>();

    public Cookie(String cookie) {
        String[] splitAttributes = cookie.split(";");
        for (String attribute : splitAttributes) {
            String[] attributeKeyAndValue = attribute.split("=");
            attributes.put(attributeKeyAndValue[0].trim(), attributeKeyAndValue[1].trim());
        }
    }

    public Optional<String> getAttribute(String key) {
        return Optional.ofNullable(attributes.get(key));
    }
}
