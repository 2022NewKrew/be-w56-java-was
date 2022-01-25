package frontcontroller;

import frontcontroller.controller.BaseController;
import frontcontroller.controller.MemberFormController;
import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MyHttpResponseStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrontController {

    private final Map<String, MyController> controllerMapping = new HashMap<>();

    public FrontController() {
        controllerMapping.put("/user/create", new MemberFormController());
        controllerMapping.put("/index.html", new BaseController());
        controllerMapping.put("", new BaseController());
    }

    public void service(MyHttpRequest request, MyHttpResponse response) throws IOException {

        String requestURI = request.getRequestURI();

        MyController controller = controllerMapping.get(requestURI);

        if (controller == null) {
            response.setStatus(MyHttpResponseStatus.NOT_FOUND);
            return;
        }
        
        response.setStatus(MyHttpResponseStatus.OK);
        controller.process(request, response);

    }
}
