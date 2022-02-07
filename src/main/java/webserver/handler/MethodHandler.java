package webserver.handler;

import org.codehaus.jackson.map.ObjectMapper;
import webserver.annotations.ResponseBody;
import webserver.annotations.RestController;
import webserver.context.Json;
import webserver.context.ServletRequest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MethodHandler {

    private final Object object;

    private final Method method;

    private final List<ParameterHandler> parameterHandlers;

    private final ObjectMapper mapper = new ObjectMapper();

    private MethodHandler(Object object, Method method, List<ParameterHandler> parameterHandlers) {
        this.object = object;
        this.method = method;
        this.parameterHandlers = parameterHandlers;
    }

    public static MethodHandler of(Object object, Method method) {
        return new MethodHandler(object, method, Arrays.stream(method.getParameters()).map(ParameterHandler::of).collect(Collectors.toUnmodifiableList()));
    }

    public Object invokeMethodBy(ServletRequest servletRequest) throws InvocationTargetException, IllegalAccessException {
        Object ret = method.invoke(object, parameterHandlers.stream().map(x -> x.parseParameter(servletRequest)).toArray());
        if (checkRestApi()) return new Json(ObjectToJson(ret));
        return ret;
    }

    public boolean checkRestApi() {
        return object.getClass().isAnnotationPresent(RestController.class) || object.getClass().isAnnotationPresent(ResponseBody.class) || method.isAnnotationPresent(ResponseBody.class);
    }

    public String ObjectToJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }
}
