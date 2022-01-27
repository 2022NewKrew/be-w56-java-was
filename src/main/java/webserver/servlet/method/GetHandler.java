package webserver.servlet.method;

import app.user.adapter.in.SignUpController;
import java.io.File;
import java.io.IOException;
import webserver.WebServerConfig;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.servlet.FileResponsible;
import webserver.servlet.FileServlet;

public class GetHandler implements MethodHandler {

    private final FileResponsible fileResponsible = new FileServlet();
    private final SignUpController signUpController;

    public GetHandler(SignUpController signUpController) {
        this.signUpController = signUpController;
    }

    @Override
    public HttpResponse handle(HttpRequest request, HttpResponse response) throws IOException {
        File file = new File(WebServerConfig.BASE_PATH + request.getUri());
        if (file.exists()) {
            return fileResponsible.write(response, file);
        }

        if (request.getUri().startsWith(SignUpController.path)) {
            return signUpController.call(request, response);
        }
        return response;
    }
}
