package frontcontroller;

import frontcontroller.controller.BaseController;
import frontcontroller.controller.MemberFormController;
import frontcontroller.controller.MemberLoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MyHttpResponseStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrontController {

    private static final Logger log = LoggerFactory.getLogger(FrontController.class);
    private final Map<String, MyController> controllerMapping = new HashMap<>();

    public FrontController() {
        // controller
        controllerMapping.put("/user/create", new MemberFormController());
        controllerMapping.put("/user/login", new MemberLoginController());
        // static path 정의
        // TODO: 추상화
        controllerMapping.put("/index.html", new BaseController("/index.html"));
        controllerMapping.put("/user/form.html", new BaseController("/user/form.html"));
        controllerMapping.put("/css/bootstrap.min.css", new BaseController("/css/bootstrap.min.css"));
        controllerMapping.put("/css/styles.css", new BaseController("/css/styles.css"));
        controllerMapping.put("/js/jquery-2.2.0.min.js", new BaseController("/js/jquery-2.2.0.min.js"));
        controllerMapping.put("/js/bootstrap.min.js", new BaseController("/js/bootstrap.min.js"));
        controllerMapping.put("/js/scripts.js", new BaseController("/js/scripts.js"));
        controllerMapping.put("/favicon.ico", new BaseController("/favicon.ico"));
    }

    public void service(MyHttpRequest request, MyHttpResponse response) throws IOException {

        String requestURI = request.getRequestURI();

        log.debug("requestURI : {}", requestURI);

        MyController controller = controllerMapping.get(requestURI);

        if (controller == null) {
            response.setStatus(MyHttpResponseStatus.NOT_FOUND);
            return;
        }

        response.setStatus(MyHttpResponseStatus.OK);
        controller.process(request, response);

    }
}
