package webapplication.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Model {

    private final Map<String, Object> valueMap;

    public Model() {
        this.valueMap = new HashMap<>();
    }

    public void addAttribute(String attributeName, Object attributeValue) {
        valueMap.put(attributeName, attributeValue);
    }

    public Optional<Object> getAttribute(String attributeName) {
        return Optional.ofNullable(valueMap.get(attributeName));
    }

    public boolean containsAttributes(String attributeName) {
        return valueMap.containsKey(attributeName);
    }

    public boolean isCookies() {
        return containsAttributes(AttributeTypes.COOKIES.getCode());
    }
}
