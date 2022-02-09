package data.converter;

import util.converter.Converter;

import java.lang.reflect.Field;
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
            for (Field field : from.getClass().getFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                String value = field.get(from).toString();
                returned.put(fieldName, value);
            }
        }catch (IllegalAccessException exception){
            throw new IllegalStateException("cannot convert");
        }

        return returned;
    }
}