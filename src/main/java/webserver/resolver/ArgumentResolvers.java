package webserver.resolver;

import com.google.common.collect.Lists;
import http.HttpRequest;
import http.HttpResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class ArgumentResolvers {

    private static final List<HandlerMethodArgumentResolver> resolvers = Lists.newArrayList();

    static {
        resolvers.add(new SessionArgumentResolver());
        resolvers.add(new ControllerArgumentResolver());
    }

    private ArgumentResolvers() {
    }

    public static Object[] resolve(Method method, HttpRequest httpRequest,
        HttpResponse httpResponse) throws Exception {
        List<Object> args = Lists.newArrayList();
        for (Class<?> parameterType : method.getParameterTypes()) {
            if (parameterType.equals(httpResponse.getClass())) {
                args.add(httpResponse);
                continue;
            }
            args.add(getArgument(parameterType, httpRequest));
        }
        return args.toArray();
    }

    private static Object getArgument(Class<?> parameterType, HttpRequest httpRequest)
        throws Exception {
        Constructor<?> declaredConstructor = parameterType.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        Object instance = declaredConstructor.newInstance();
        for (HandlerMethodArgumentResolver resolver : resolvers) {
            if (resolver.supportsParameter(parameterType, httpRequest)) {
                instance = resolver.resolveArgument(instance, httpRequest);
                break;
            }
        }
        return instance;
    }
}
