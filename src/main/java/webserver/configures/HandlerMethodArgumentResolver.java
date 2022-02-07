package webserver.configures;

public interface HandlerMethodArgumentResolver {

    default boolean supportsParameter(MethodParameter parameter) {
        return false;
    }

    default Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return null;
    }

}
