package webserver.http.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.request.Method;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import static util.HttpRequestUtils.urlToFile;

public class StaticController implements HttpController {
    private static final Logger log = LoggerFactory.getLogger(StaticController.class);

    @Override
    public boolean isValidRequest(HttpRequest request) {
        Method method = request.getMethod();
        String url = request.getUrl();
        if (Objects.equals(url, "/user/list.html")) {
            return false;
        }
        return (method == Method.GET && urlToFile(url).toFile().exists());
    }

    @Override
    public HttpResponse handleRequest(HttpRequest request, OutputStream out) throws IOException {
        log.debug("staticController handle request");
        return HttpResponseUtils.ok(out, request);
    }
}
