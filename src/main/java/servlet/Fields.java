package servlet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Fields {
    private final Map<String, Class<?>> fields;

    private Fields(Map<String, Class<?>> fields) {
        this.fields = fields;
    }

    public static Fields create(Field[] fields) {
        Map<String, Class<?>> fieldMap = new LinkedHashMap<>();
        for (Field field : fields)
            fieldMap.put(field.getName(), field.getType());
        return new Fields(fieldMap);
    }

    public Constructor createConstructor(Class<?> parameter) {
        // TODO 예외처리
        try {
            return parameter.getDeclaredConstructor(fields.values().toArray(Class<?>[]::new));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object[] makeFieldObjects(Map<String, String> userDto) {
        return fields.keySet()
                .stream()
                .map(userDto::get)
                .toArray(Object[]::new);
    }
}
