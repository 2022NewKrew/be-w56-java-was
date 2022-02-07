package webserver.configures;

public interface MethodParameter {
    boolean hasParameterAnnotation(Class<?> parameterClazz);

    Class<?> getParameterType();
}
