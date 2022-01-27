package webserver.servlet;

import app.user.adapter.in.SignUpController;
import app.user.application.port.SignUpService;
import app.user.application.port.in.CreateUserUseCase;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseStatus;
import webserver.servlet.method.GetHandler;

public class HttpHandler implements HttpHandleable {

    private final RequestLogger logger = new RequestLogger();
    private final GetHandler getHandler;
    private final CreateUserUseCase createUserUseCase;
    private final SignUpController signUpController;

    private HttpHandler() {
        this.createUserUseCase = new SignUpService();
        this.signUpController = new SignUpController(createUserUseCase);

        this.getHandler = new GetHandler(signUpController);
    }

    public static HttpHandler getInstance() {
        return HttpHandlerHolder.INSTANCE;
    }

    @Override
    public HttpResponse handle(HttpRequest request, HttpResponse response) {
        try {
            logger.request(request);
            switch (request.getMethod()) {
                case GET:
                    response = getHandler.handle(request, response);
                case POST:
                    break;
            }
        } catch (Exception e) {
            response.setStatus(HttpResponseStatus.INTERNAL_ERROR);
        }
        return response;
    }

    private static class HttpHandlerHolder {

        private static final HttpHandler INSTANCE = new HttpHandler();
    }

}
