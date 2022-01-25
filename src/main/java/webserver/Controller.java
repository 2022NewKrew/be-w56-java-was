package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.BiFunction;
import java.util.function.Function;

import webserver.request.HttpRequest;
import webserver.request.RequestHeader;
import webserver.request.RequestLine;
import webserver.response.HttpResponse;
import webserver.response.Status;

public class Controller {
    public HttpResponse handle(HttpRequest httpRequest) {

        // 임시 Controller
        if (httpRequest.getPath().equals("/")) {
            return HttpResponse.of("/index.html", Status.OK, null);
        }

        File file = new File("./webapp" + httpRequest.getPath());
        if (!file.exists() || file.isDirectory()) {
            return HttpResponse.of("", Status.NOT_FOUND, null);
        }

        return HttpResponse.of(httpRequest.getPath(), Status.OK, null);
    }
}
