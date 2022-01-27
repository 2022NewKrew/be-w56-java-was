package framework.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 요청 정보에 Body가 있는 경우, 해당 정보들을 담을 일급 컬렉션 클래스
 */
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
