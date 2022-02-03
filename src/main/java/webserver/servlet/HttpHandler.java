package webserver.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.servlet.method.GetHandler;
import webserver.servlet.method.PostHandler;

public class HttpHandler implements HttpHandleable {

    private final ApplicationContext applicationContext;
    private final Logger logger = LoggerFactory.getLogger(HttpHandler.class);
    private final HttpHandleable getHandler;
    private final HttpHandleable postHandler;

    private HttpHandler() {
        this.applicationContext = ApplicationContext.getInstance();
        this.getHandler = new GetHandler(applicationContext);
        this.postHandler = new PostHandler(applicationContext);
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
                    postHandler.handle(request, response);
                    break;
            }
            response.send();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private static class HttpHandlerHolder {

        private static final HttpHandler INSTANCE = new HttpHandler();
    }

}
