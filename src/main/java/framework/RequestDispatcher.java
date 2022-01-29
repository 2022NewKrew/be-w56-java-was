package framework;

import org.slf4j.LoggerFactory;

import java.io.IOException;

import org.slf4j.Logger;
import framework.handler.Handler;
import util.HttpRequest;
import util.HttpResponse;

public class RequestDispatcher {

    public static Logger log = LoggerFactory.getLogger(RequestDispatcher.class);

    private final HandlerMapping handlerMapping;
    private final ViewResolver viewResolver;

    public RequestDispatcher(HandlerMapping handlerMapping, ViewResolver viewResolver) {
        this.handlerMapping = handlerMapping;
        this.viewResolver = viewResolver;
    }

    private int DATA_SIZE = 2000000;

    public void doDispatch(HttpRequest req, HttpResponse res) throws IOException {

        Handler mappedHandler = handlerMapping.getHandler(req);

        if (mappedHandler == null) {
            log.error("지원하는 Handler 가 없습니다. : " + req.getPath());
            res.setStatusCode(404);
            return ;
        }


        String viewName = mappedHandler.handle(req, res);
        viewResolver.render(viewName, req, res);

    }
}
