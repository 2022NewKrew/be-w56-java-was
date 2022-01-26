package springmvc.frontcontroller;

import springmvc.adapter.ControllerHandlerAdapter;
import springmvc.adapter.CustomHandlerAdapter;
import springmvc.controller.UserSaveController;
import webserver.http.request.CustomHttpMethod;
import webserver.http.request.CustomHttpRequest;
import webserver.http.response.CustomHttpResponse;
import webserver.http.response.CustomHttpStatus;

import javax.management.ServiceNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrontController {
    private final Map<HandleMappingKey, Object> handlerMappingMap = new HashMap<>();
    private final List<CustomHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontController() {
        initHandlerMapping();
        initHandlerAdapters();
    }

    private void initHandlerMapping() {
        handlerMappingMap.put(new HandleMappingKey("/create", CustomHttpMethod.GET), new UserSaveController());
        handlerMappingMap.put(new HandleMappingKey("/create", CustomHttpMethod.POST), new UserSaveController());
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerHandlerAdapter());
    }

    public String service(CustomHttpRequest request, CustomHttpResponse response) throws ServiceNotFoundException {
        Object handler = getHandler(request);
        if (handler == null) {
            response.setHttpStatus(CustomHttpStatus.NOT_FOUND);

            throw new ServiceNotFoundException("404 not found, there is no handler for uri : " + request.getRequestURI());
        }

        CustomHandlerAdapter adapter = getHandlerAdapter(handler);
        CustomModelView mv = adapter.handle(request, response, handler);

        CustomView view = viewResolver(mv.getViewName());

        return view.getViewPath();
    }

    private Object getHandler(CustomHttpRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getRequestLineMap().get("method");
        CustomHttpMethod httpMethod = CustomHttpMethod.findHttpMethodByName(method);

        return handlerMappingMap.get(new HandleMappingKey(requestURI, httpMethod));
    }

    private CustomHandlerAdapter getHandlerAdapter(Object handler) {
        for (CustomHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("there is no that handler adapter, handler=" + handler);
    }

    private CustomView viewResolver(String viewName) {
        return new CustomView("./webapp" + viewName + ".html");
    }
}
