package webserver.dispatcher.dynamic.bind.handler;

import java.lang.reflect.Method;

public class ClassAndMethod {
    private Class<?> clazz;
    private Method method;

    public ClassAndMethod(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Method getMethod() {
        return method;
    }
}
