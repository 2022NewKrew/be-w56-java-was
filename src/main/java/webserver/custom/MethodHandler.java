package webserver.custom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodHandler {

    private final Object object;

    private final Method method;

    public MethodHandler(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    public void invokeMethod(Object[] args) throws InvocationTargetException, IllegalAccessException {
        method.invoke(object, args);
    }

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }
}
