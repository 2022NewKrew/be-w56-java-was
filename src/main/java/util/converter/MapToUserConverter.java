package util.converter;


import webserver.domain.entity.User;

import java.util.Map;

class MapToUserConverter implements Converter<Map<String, String>, User>{
    @Override
    public boolean support(Class<?> fromType, Class<?> toType) {
        return TypeChecker.equals(fromType, Map.class)
                && TypeChecker.equals(toType, User.class);
    }

    @Override
    public User convert(Map<String, String> from)  {
        return new User(
                from.get("userId"),
                from.get("password"),
                from.get("name"),
                from.get("email"));
    }
}
