package webserver.http.request;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Method {
    GET,
    POST;

    private static final Map<String, Method> stringToMethod = new HashMap<>();

    static {
        for (Method method : values()) {
            stringToMethod.put(method.name(), method);
        }
    }

    public static Method getMethodFromString(String str) {
        return stringToMethod.get(str);
    }

    public static boolean contain(Method method) {
        return Arrays.asList(values()).contains(method);
    }
}
