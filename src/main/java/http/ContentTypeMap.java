package http;

import java.util.HashMap;
import java.util.Map;

public class ContentTypeMap {
    private static final Map<String, ContentType> reverseMap = new HashMap<>();

    static {
        reverseMap.put("css", ContentType.CSS);
        reverseMap.put("javascript", ContentType.JS);
        reverseMap.put("html", ContentType.HTML);
    }

    public static ContentType get(String key) {
        if (!reverseMap.containsKey(key)) {
            return ContentType.DEFAULT;
        }

        return reverseMap.get(key);
    }
}
