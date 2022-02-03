package webserver.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseStatus;
import webserver.servlet.method.GetHandler;

public class HttpHandler implements HttpHandleable {

    private final ApplicationContext applicationContext;
    private final Logger logger = LoggerFactory.getLogger(HttpHandler.class);
    private final GetHandler getHandler;

    private HttpHandler() {
        this.applicationContext = ApplicationContext.getInstance();
        this.getHandler = new GetHandler(applicationContext);
    }

    public static HttpHandler getInstance() {
        return HttpHandlerHolder.INSTANCE;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        try {
            logger.info("{} {}", request.getMethod(), request.getUri());
            switch (request.getMethod()) {
                case GET:
                    getHandler.handle(request, response);
                    break;
                case POST:
                    break;
            }
            response.send();
        } catch (Exception e) {
            logger.info("{}", e.getMessage());
            response.setStatus(HttpResponseStatus.INTERNAL_ERROR);
        }
    }

    private static class HttpHandlerHolder {

        private static final HttpHandler INSTANCE = new HttpHandler();
    }

}
