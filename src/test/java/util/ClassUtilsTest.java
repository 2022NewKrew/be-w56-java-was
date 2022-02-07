package util;

import annotation.Controller;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ClassUtilsTest {

    @Test
    void testFindAnnotatedClasses() {
        Set<Class<?>> classes = ClassUtils.findAnnotatedClasses(Controller.class);
        classes.stream()
                .filter(c -> c.getName().contains("UserController"))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Failed to find user controller"));
    }
}