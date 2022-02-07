package webserver.method;

import java.lang.reflect.Method;

public class RequestMappingHandler {
    private final String beanName;
    private final Method method;

    public RequestMappingHandler(String beanName, Method method) {
        this.beanName = beanName;
        this.method = method;
    }

    public String getBeanName() {
        return beanName;
    }

    public Method getMethod() {
        return method;
    }
}
