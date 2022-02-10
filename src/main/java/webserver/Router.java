package webserver;

import controller.StaticResourceController;
import controller.UserController;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtils;
import webserver.request.Request;
import webserver.response.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public class Router {
    private static final Logger log = LoggerFactory.getLogger(Router.class);
    private static final UserController userController = UserController.getInstance();
    private static final StaticResourceController staticResourceController = StaticResourceController.getInstance();

    private static Router instance;

    private Router() {}

    public static synchronized Router getInstance() {
        if(instance == null) {
            instance = new Router();
        }
        return instance;
    }

    public void routing(DataOutputStream dos, Request request) throws ParseException, IOException {
        String path = request.getRequestLine().getRequestUri().getPath();
        Response response = null;

        if(path.startsWith("/user")) {
            response = userController.handle(request);
        }

        if (response != null) {
            log.info("Url Matching! response: {}", response);
            HttpResponseUtils.setDataOutputStream(dos, response);
            return;
        }
        // 맵핑 되는 url 없으면 스태틱 리소스 확인, 해당 파일 없으면 에러 처리
        try {
            log.info("Static Resource");
            HttpResponseUtils.setDataOutputStream(dos, staticResourceController.requestStaticResource(request));
        } catch (Exception e){
            log.info("Bad Request");
            HttpResponseUtils.setDataOutputStream(dos, HttpResponseUtils.createErrorResponse());
        }
    }

}
