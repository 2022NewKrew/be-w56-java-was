package framework.handler;

import util.HttpRequest;
import util.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

public class RequestMappingHandler extends Handler{

    private final List<HandlerMethod> handlerMethods;

    // ServerConfig 에서 handlerMethods 주입됨
    public RequestMappingHandler(List<HandlerMethod> handlerMethods) {
        this.handlerMethods = handlerMethods;
    }

    @Override
    public String handle(HttpRequest req, HttpResponse res) {
        HandlerMethod method = lookupHandlerMethod(req).get();
        Object returnValue = null;
        try {
            // Controller 의 Method 호출
            // view 네임 반환됨
            returnValue = method.getHandleMethod().invoke(method.getBean(), req, res);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (String) returnValue;
    }

    @Override
    public boolean isSupport(HttpRequest req) {
        return lookupHandlerMethod(req).isPresent();
    }

    // HTTP Method 와 path 로 handlerMethod 검색
    private Optional<HandlerMethod> lookupHandlerMethod(HttpRequest req) {
        return handlerMethods.stream()
                .filter(h -> h.matchRequest(req))
                .findAny();
    }
}
