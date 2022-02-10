package webserver.handler;

import java.util.List;
import java.util.Map;

public class ParsedParams {
    public static final List<Class> SUPPORTED_TYPE = List.of(
            new Class[]{boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class,
                    Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class, String.class});
    private final Map<String, Object> paramNameToValue;

    public ParsedParams() {
        paramNameToValue = null;
    }

    public ParsedParams(Map<String, Object> paramNameToValue) {
        this.paramNameToValue = paramNameToValue;
    }

    public Object get(String paramName) {
        return paramNameToValue.get(paramName);
    }

    public boolean isValid() {
        return paramNameToValue != null;
    }
}
