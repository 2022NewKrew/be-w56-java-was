package webserver.servlet.method;

import app.user.adapter.in.SignUpController;
import java.io.File;
import java.io.IOException;
import webserver.WebServerConfig;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.servlet.FileHandleable;
import webserver.servlet.FileHandler;

public class GetHandler implements MethodHandler {

    private final FileHandleable fileHandleable = new FileHandler();
    private final SignUpController signUpController;

    public GetHandler(SignUpController signUpController) {
        this.signUpController = signUpController;
    }

    @Override
    public HttpResponse handle(HttpRequest request, HttpResponse response) throws IOException {
        File file = new File(WebServerConfig.BASE_PATH + request.getUri());
        if (file.exists()) {
            return fileHandleable.write(response, file);
        }

        if (request.getUri().startsWith(SignUpController.path)) {
            return signUpController.call(request, response);
        }
        return response;
    }
}
