package framework.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
    enum ComponentType {
        CONTROLLER, SERVICE, REPOSITORY, CONFIGURATION, OTHER
    }

    ComponentType type() default ComponentType.OTHER;
}
