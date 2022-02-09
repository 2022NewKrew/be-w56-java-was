package util.converter;

import webserver.domain.entity.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

class MapToPostConverter implements Converter<Map<String,String>, Post>{
    @Override
    public boolean support(Class<?> fromType, Class<?> toType) {
        return TypeChecker.equals(fromType, Map.class)
                && TypeChecker.equals(toType, Post.class);
    }

    @Override
    public Post convert(Map<String, String> from) {
        return new Post(
                from.get("writer"),
                from.get("content"),
                LocalDateTime.parse(from.get("written"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
}
