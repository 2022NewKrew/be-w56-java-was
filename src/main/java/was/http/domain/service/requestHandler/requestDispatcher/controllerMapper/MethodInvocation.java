package was.http.domain.service.requestHandler.requestDispatcher.controllerMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvocation {
    private final Object real;
    private final Method method;

    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    public MethodInvocation(Object real, Method method) {
        this.real = real;
        this.method = method;
    }

    public Object invoke(Object... objects) {
        try {
            return method.invoke(real, objects);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException();
        }
    }
}
