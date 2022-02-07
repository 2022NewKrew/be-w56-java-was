package webserver.handler;

import webserver.configures.HandlerMethodArgumentResolver;
import webserver.configures.MethodParameter;
import webserver.context.ServletRequest;

import java.util.ArrayList;
import java.util.List;

public class ArgumentResolverHandler {

    private static ArgumentResolverHandler argumentResolverHandler;

    private List<HandlerMethodArgumentResolver> argumentResolvers;

    private ArgumentResolverHandler(List<HandlerMethodArgumentResolver> argumentResolvers) {
        this.argumentResolvers = argumentResolvers;
    }

    public static ArgumentResolverHandler getInstance() {
        if (argumentResolverHandler == null) {
            argumentResolverHandler = new ArgumentResolverHandler(new ArrayList<>());
        }
        return argumentResolverHandler;
    }

    public static Object resolveParameter(MethodParameter methodParameter, ServletRequest servletRequest) {
        for (HandlerMethodArgumentResolver resolver : argumentResolverHandler.getArgumentResolvers()) {
            if (resolver.supportsParameter(methodParameter)) {
                return resolver.resolveArgument(methodParameter, null, servletRequest, null);
            }
        }
        return null;
    }

    public List<HandlerMethodArgumentResolver> getArgumentResolvers() {
        return argumentResolvers;
    }
}
