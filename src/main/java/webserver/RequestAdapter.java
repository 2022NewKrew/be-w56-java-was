package webserver;

import lombok.extern.slf4j.Slf4j;
import webserver.controller.Controller;
import webserver.web.Parameters;
import webserver.web.request.Request;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class RequestAdapter {

    private static final RequestAdapter adapter = new RequestAdapter();

    private RequestAdapter() {

    }

    public static RequestAdapter getInstance() {
        return adapter;
    }

    public ModelAndView handle(Controller controller, Request request) throws InvocationTargetException, IllegalAccessException {
        Parameters parameters = new Parameters(request);
        Object result = controller.handle(request, parameters);

        if (result == null) {
            return null;
        }
        if (result.getClass().equals(String.class)) {
            return new ModelAndView((String) result, request.getModel(), parameters.getResponseBuilder());
        }
        /*if(result.getClass().equals()) {

        }*/

        return null;
    }
}
