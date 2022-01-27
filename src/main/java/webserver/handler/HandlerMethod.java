package webserver.handler;

import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class HandlerMethod {
    private final Class<?> clazz;
    private final Method method;

    public static HandlerMethod of(Class<?> clazz, Method method) {
        return new HandlerMethod(clazz, method);
    }

    private HandlerMethod(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }
}
