package webserver;

import com.google.common.collect.Lists;
import http.HttpRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ArgumentResolvers {

    private static final List<HandlerMethodArgumentResolver> resolvers = Lists.newArrayList();

    static {
        resolvers.add(new ControllerArgumentResolver());
    }

    public static Object[] resolve(Method method, HttpRequest httpRequest) throws Exception {
        List<Object> args = new ArrayList<>();
        for (Class<?> parameterType : method.getParameterTypes()) {
            args.add(getObject(parameterType, httpRequest));
        }
        return args.toArray();
    }

    private static Object getObject(Class<?> parameterType, HttpRequest httpRequest)
        throws Exception {
        Object instance = parameterType.getDeclaredConstructor().newInstance();
        for (HandlerMethodArgumentResolver resolver : resolvers) {
            if (resolver.supportsParameter(parameterType, httpRequest)) {
                instance = resolver.resolveArgument(instance, httpRequest);
                break;
            }
        }
        return instance;
    }
}
