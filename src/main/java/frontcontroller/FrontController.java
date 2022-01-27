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

    private static final int HAVE_EXTENSION = -1;
    private static final Logger log = LoggerFactory.getLogger(FrontController.class);
    private final Map<String, MyController> controllerMapping = new HashMap<>();

    public FrontController() {
        // controller
        controllerMapping.put("/user/create", new MemberFormController());
        controllerMapping.put("/user/login", new MemberLoginController());
        controllerMapping.put("/", new BaseController("/index.html"));
    }

    public void service(MyHttpRequest request, MyHttpResponse response) throws IOException {

        String requestURI = request.getRequestURI();

        log.debug("requestURI : {}", requestURI);

        MyController controller = getMapping(requestURI);

        if (controller == null) {
            response.setStatus(MyHttpResponseStatus.NOT_FOUND);
            return;
        }

        response.setStatus(MyHttpResponseStatus.OK);
        controller.process(request, response);

    }

    private MyController getMapping(String requestURI) {
        int extensionOf = requestURI.lastIndexOf('.');

        if (haveExtension(extensionOf)) {
            return new BaseController(requestURI);
        }

        return controllerMapping.get(requestURI);
    }

    private boolean haveExtension(int extensionOf) {
        return extensionOf != HAVE_EXTENSION;
    }
}
