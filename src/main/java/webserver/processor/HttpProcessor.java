package webserver.processor;

import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.routes.Router;
import webserver.reader.StaticFileHandler;

public class HttpProcessor {

    private static final Logger log = LoggerFactory.getLogger(HttpProcessor.class);

    /**
     * 1. 동적 어플리케이션에서 처리 가능한지 확인
     * 2. 1번에서 없다면, 정적파일 처리기에서 처리 가능한지 확인
     * 3. 없다면, Page Not Found
     */
    public static HttpResponse handle(HttpRequest httpRequest) {
        Router router = Router.getInstance();
        if(router.canRoute(httpRequest)) {
            return router.route(httpRequest);
        }
        return StaticFileHandler.handle(httpRequest);
    }
}
