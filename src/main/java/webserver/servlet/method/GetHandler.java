package webserver.servlet.method;

import app.user.adapter.in.SignUpController;
import java.io.File;
import java.io.IOException;
import webserver.WebServerConfig;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.servlet.FileHandleable;
import webserver.servlet.FileHandler;
import webserver.servlet.HttpHandleable;

public class GetHandler implements HttpHandleable {

    private final FileHandleable fileHandleable = new FileHandler();
    private final SignUpController signUpController;

    public GetHandler(SignUpController signUpController) {
        this.signUpController = signUpController;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        String uriPath = WebServerConfig.BASE_PATH + request.getUri();
        File file = new File(uriPath);
        File entryFile = new File(uriPath + WebServerConfig.ENTRY_FILE);

        if (file.exists()) {
            fileHandleable.write(response, file);
            return;
        }

        if (entryFile.exists()) {
            fileHandleable.write(response, entryFile);
            return;
        }

        if (request.getUri().startsWith(SignUpController.path)) {
            signUpController.call(request, response);
        }
    }
}
