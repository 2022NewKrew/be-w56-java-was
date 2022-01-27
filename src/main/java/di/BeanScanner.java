package di;

import di.annotation.Bean;
import org.reflections.Reflections;

import java.util.Set;

public class BeanScanner {
    public static Set<Class<?>> findBean(String packageName) {
        final Reflections reflections = new Reflections(packageName);

        return reflections.getTypesAnnotatedWith(Bean.class);
    }
}
