package util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ControllerUtils {
    public static Map<String, String> getEmptyCookieMap(){
        return Collections.unmodifiableMap(new HashMap<>());
    }

    public static Map<String, Object> getEmptyModelMap() {
        return Collections.unmodifiableMap(new HashMap<>());
    }
}
