package webserver.servlet;

import app.user.adapter.in.SignUpController;
import app.user.application.port.SignUpService;
import app.user.application.port.in.CreateUserUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseStatus;
import webserver.servlet.method.GetHandler;

public class HttpHandler implements HttpHandleable {

    private final Logger logger = LoggerFactory.getLogger(HttpHandler.class);
    private final GetHandler getHandler;
    private final CreateUserUseCase createUserUseCase;
    private final SignUpController signUpController;

    //TODO: controllers

    private HttpHandler() {
        this.createUserUseCase = new SignUpService();
        this.signUpController = new SignUpController(createUserUseCase);

        this.getHandler = new GetHandler(signUpController);
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
