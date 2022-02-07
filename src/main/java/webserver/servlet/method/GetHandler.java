package webserver.servlet.method;

import java.io.File;
import java.io.IOException;
import webserver.WebServerConfig;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.servlet.FileHandleable;
import webserver.servlet.FileHandler;
import webserver.servlet.HttpControllable;
import webserver.servlet.HttpHandleable;
import webserver.servlet.RequestMapping;

public class GetHandler implements HttpHandleable {

    private final FileHandleable fileHandleable = new FileHandler();
    private final RequestMapping requestMapping;

    public GetHandler(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        String uriPath = WebServerConfig.BASE_PATH + request.getUri();
        File file = new File(uriPath);

        if (file.isDirectory()) {
            File entryFile = new File(uriPath + WebServerConfig.ENTRY_FILE);
            fileHandleable.write(response, entryFile);
            return;
        }

        if (file.exists()) {
            fileHandleable.write(response, file);
            return;
        }

        HttpControllable controller = requestMapping.getHandler(request.getUri());
        controller.call(request, response);
    }
}