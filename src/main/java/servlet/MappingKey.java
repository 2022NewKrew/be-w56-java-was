package servlet;

import annotation.GetMapping;
import annotation.PostMapping;

import java.lang.reflect.Method;
import java.util.Objects;

public class MappingKey {
    private final String key;

    public MappingKey(String key) {
        this.key = key;
    }

    public static MappingKey create(Method method) {
        String key = "";
        if (method.isAnnotationPresent(GetMapping.class)) {
            key = "GET" + method.getAnnotation(GetMapping.class).value();
        }

        if (method.isAnnotationPresent(PostMapping.class)) {
            key = "POST" + method.getAnnotation(PostMapping.class).value();
        }
        return new MappingKey(key);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        MappingKey Mappingkey = (MappingKey) object;
        return Objects.equals(this.key, Mappingkey.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
