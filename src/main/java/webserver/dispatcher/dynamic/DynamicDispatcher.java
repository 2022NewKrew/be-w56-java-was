package webserver.dispatcher.dynamic;

import webserver.container.ControllerContainer;
import webserver.dispatcher.Dispatcher;
import webserver.dispatcher.dynamic.bind.handler.ClassAndMethod;
import webserver.request.HttpRequest;
import webserver.request.RequestContext;
import webserver.response.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

// todo
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
    protected HttpResponse createResponse() {
        HttpRequest request = RequestContext.getInstance().getHttpRequest();
        // todo RequestMethodNotFoundException 처리 로직
        ClassAndMethod classAndMethod = HandlerMapping.getInstance().getControllerMethodForRequest();

        // todo 해당하는 Controller 없을 때 예외 처리 로직
        Object controller = ControllerContainer.getInstance().getControllerInstance(classAndMethod.getClazz());

        Object responseBody = null;
        // todo try catch 문 정리
        try {
            responseBody = classAndMethod.getMethod().invoke(controller);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return new HttpResponse(new HttpResponseLine(HttpStatus.OK), new HttpResponseHeader(new HashMap<>()), new HttpResponseBody(new byte[]{}));
    }


}
