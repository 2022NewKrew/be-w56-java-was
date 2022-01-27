package framework.util;

import java.util.HashMap;
import java.util.Map;

/**
 * ModelView에서 활용할 attributes 정보들을 담을 일급 컬렉션 클래스
 */
public class ModelViewAttributes {
    private final Map<String, Object> modelViewAttributes = new HashMap<>();

    public Object getAttribute(String key) {
        return modelViewAttributes.get(key);
    }

    public void setAttribute(String key, Object value) {
        modelViewAttributes.put(key, value);
    }

    /**
     * 현재 attributes 정보들을 JSON 형태의 String으로 파싱하여 반환해주는 메소드
     * @return 파싱된 JSON 형태의 String
     */
    public String parseAttributesToJson() {
        return "";
    }
}
