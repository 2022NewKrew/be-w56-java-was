package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping
public @interface GetMapping {

    @AliasFor(annotation = RequestMapping.class)
    String value() default  "";

    @AliasFor(annotation = RequestMapping.class)
    String path() default  "";
}
