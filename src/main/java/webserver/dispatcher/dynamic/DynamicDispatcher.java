package webserver.dispatcher.dynamic;

import webserver.container.ControllerContainer;
import webserver.dispatcher.Dispatcher;
import webserver.dispatcher.dynamic.bind.handler.ClassAndMethod;
import webserver.exception.ControllerAccessDeniedException;
import webserver.json.JsonParser;
import webserver.response.ContentType;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseHeader;
import webserver.response.ResponseContext;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

/**
 * Singleton
 */
public class DynamicDispatcher extends Dispatcher {

    private static final DynamicDispatcher INSTANCE = new DynamicDispatcher();

    private DynamicDispatcher() {
    }

    public static DynamicDispatcher getInstance() {
        return INSTANCE;
    }

    @Override
    protected HttpResponse processRequest() {
        ClassAndMethod classAndMethod = getClassAndMethodForRequest();
        Object controller = ControllerContainer.getInstance().getControllerInstance(classAndMethod.getClazz());

        try {
            Object responseBody = classAndMethod.getMethod().invoke(controller);
            if (responseBody != null) {
                writeResponseBodyToResponse(responseBody);
            }
        } catch (IllegalAccessException e) {
            throw new ControllerAccessDeniedException();
        } catch (InvocationTargetException e) {
            new RuntimeException(e.getTargetException());
        }

        return ResponseContext.getInstance().getHttpResponse();
    }

    private ClassAndMethod getClassAndMethodForRequest() {
        return HandlerMapping.getInstance().getControllerClassAndMethodForRequest();
    }

    private void writeResponseBodyToResponse(Object responseBody) {
        HttpResponse response = ResponseContext.getInstance().getHttpResponse();
        String jsonString = JsonParser.getInstance().objectToJsonString(responseBody);
        response.setResponseBody(jsonString.getBytes(StandardCharsets.UTF_8));
        response.setHeaderIfAbsent(HttpResponseHeader.KEY_FOR_CONTENT_TYPE, new String[]{ContentType.APPLICATION_JSON.getValue()});
    }
}
