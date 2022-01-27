package util.converter;


import webserver.domain.entity.User;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BodyParamsToUserConverter implements Converter<Map<String, String>, User>{
    @Override
    public boolean support(Class<?> fromType, Class<?> toType) {
        return false;
    }

    @Override
    public User convert(Map<String, String> from) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return new User(
                from.get("userId"),
                from.get("password"),
                from.get("name"),
                from.get("email"));
    }
}
