package webserver;

import error.ErrorHandler;
import lombok.extern.slf4j.Slf4j;
import webserver.controller.Controller;
import webserver.web.request.Request;
import webserver.web.response.Response;

@Slf4j
public class DispatcherServlet {

    private static final DispatcherServlet dispatcherServlet = new DispatcherServlet();
    private final RequestMapper mapper;
    private final RequestAdapter adapter;

    private DispatcherServlet() {
        mapper = RequestMapper.getInstance();
        adapter = RequestAdapter.getInstance();
    }

    public static DispatcherServlet getInstance() {
        return dispatcherServlet;
    }

    public Response serve(Request request) {
        Response response;
        try {
            Controller controller = mapper.mapping(request);
            ModelAndView modelAndView = adapter.handle(controller, request);
            response = modelAndView.getView().render();
        } catch (Exception e) {
            response = ErrorHandler.handle(e);
        }
        return response;
    }
}
