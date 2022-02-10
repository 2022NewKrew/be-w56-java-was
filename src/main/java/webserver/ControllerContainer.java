package webserver;

import CustomException.EmptyHttpRequestException;
import controller.ArticleController;
import controller.Controller;
import controller.MainController;
import controller.UserController;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerContainer {
    private static final Map<String, Controller> controllerMap = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(ControllerContainer.class);

    static {
        controllerMap.put("user", UserController.getInstance());
        controllerMap.put("articles", ArticleController.getInstance());
    }

    /**
     * @param request  http 요청 객체
     *
     * url에서 key가 되는 값을 parsing (ex. /user/create -> user)
     * 이를 담당하는 controller가 존재하면 처리 위임
     * default controller = MainController
     */
    public static HttpResponse map(HttpRequest request) {
        if (request == null) {
            throw new EmptyHttpRequestException();
        }
        String url = request.line().path();
        String[] tokens = url.split("/");
        String urlKey = "";

        if (tokens.length > 1)
            urlKey = tokens[1];

        try {
            if (!controllerMap.containsKey(urlKey))
                return MainController.getInstance().process(request); // Default Controller
            else
                return controllerMap.get(urlKey).process(request);
        } catch (RuntimeException re) {
            log.error(re.getMessage());
            return MainController.getInstance().redirect("/");
        }
    }
}
