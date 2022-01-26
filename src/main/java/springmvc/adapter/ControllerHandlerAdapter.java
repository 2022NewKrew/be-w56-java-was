package springmvc.adapter;

import springmvc.controller.CustomController;
import springmvc.frontcontroller.CustomModelView;
import webserver.http.request.CustomHttpRequest;
import webserver.http.response.CustomHttpResponse;

import java.util.HashMap;
import java.util.Map;

public class ControllerHandlerAdapter implements CustomHandlerAdapter{
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof CustomController);
    }

    @Override
    public CustomModelView handle(CustomHttpRequest request, CustomHttpResponse response, Object handler) {
        CustomController controller = (CustomController) handler;

        Map<String, String> param = request.getRequestParam();
        Map<String, Object> model = new HashMap<>();

        String viewName = controller.process(param, model);

        CustomModelView mv = new CustomModelView(viewName);
        mv.setModel(model);

        return mv;
    }
}
