package framework.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Client으로 보내는 응답의 Body에 ModelView의 Attribute 값들을 JSON 형태로 반환하도록 설정해주는 Annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ResponseBody {
}
