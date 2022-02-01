package util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ControllerUtils {
    public static Map<String, String> getEmptyImmutableMap(){
        return Collections.unmodifiableMap(new HashMap<>());
    }
}
