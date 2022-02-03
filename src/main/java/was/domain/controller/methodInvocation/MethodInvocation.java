package was.domain.controller.methodInvocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvocation {
    private final Object real;
    private final Method method;

    public MethodInvocation(Object real, Method method) {
        this.real = real;
        this.method = method;
    }

    public void invoke(Object... objects) {
        try {
            method.invoke(real, objects);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException();
        }
    }
}
