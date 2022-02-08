package framework.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 요청 정보에 Body가 있는 경우, 해당 정보들을 담을 일급 컬렉션 클래스
 */
public class RequestAttributes {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Map<String, String> requestAttributes = new HashMap<>();

    public String getAttribute(String key) {
        return requestAttributes.get(key);
    }

    public void setAttributes(String key, String value) {
        requestAttributes.put(key, value);
    }

    public void parseAttributes(String attributesStr) {
        try {
            // JSON 형태인지 확인하여 JSON 형태라면 그에 맞게 파싱
            Map<String, String> map = OBJECT_MAPPER.readValue(attributesStr, Map.class);
            requestAttributes.putAll(map);
        } catch (JsonProcessingException e) {
            // JSON 형태가 아니라면 또 그에 맞게 파싱
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
    }

    public void clear() {
        requestAttributes.clear();
    }
}
