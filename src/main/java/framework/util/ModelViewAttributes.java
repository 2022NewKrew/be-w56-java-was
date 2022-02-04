package framework.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public Set<Map.Entry<String, Object>> entrySet() {
        return modelViewAttributes.entrySet();
    }

    public boolean contains(String key) {
        return modelViewAttributes.containsKey(key);
    }

    public void copy(ModelViewAttributes forCopy) {
        modelViewAttributes.clear();

        for (Map.Entry<String, Object> entry : forCopy.entrySet()) {
            modelViewAttributes.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 현재 attributes 정보들을 JSON 형태의 String으로 파싱하여 반환해주는 메소드
     * @return 파싱된 JSON 형태의 String
     */
    public String parseAttributesToJson() {
        return "";
    }
}
