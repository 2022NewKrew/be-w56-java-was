package data.converter;

import util.converter.Converter;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ObjectToFieldMapConverter implements Converter<Object, Map<String, String>> {
    @Override
    public boolean support(Class<?> fromType, Class<?> toType) {
        return false;
    }

    @Override
    public Map<String, String> convert(Object from) {
        Map<String, String> returned = new HashMap<>();

        try {
            for (Field field : from.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                String value = getValue(field, from);
                returned.put(fieldName, value);
            }
        }catch (IllegalAccessException exception){
            throw new IllegalStateException("cannot convert");
        }

        return returned;
    }

    private static String getValue(Field field, Object obj) throws IllegalAccessException {
        Object val = field.get(obj);

        if(field.getAnnotatedType().getType().getTypeName().contains("LocalDateTime")){
            return ((LocalDateTime)val).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        return field.get(obj).toString();
    }
}