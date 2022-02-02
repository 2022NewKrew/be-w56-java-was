package webserver.http.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constants;
import webserver.http.response.ContentType;
import webserver.http.request.HttpRequest;
import webserver.http.request.Method;
import webserver.http.response.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticController implements HttpController {
    private static final Logger log = LoggerFactory.getLogger(StaticController.class);

    @Override
    public boolean isValidRequest(HttpRequest request) {
        Method method = request.getMethod();
        String url = request.getUrl();
        return (method == Method.GET && urlToFile(url).toFile().exists());
    }

    @Override
    public HttpResponse handleRequest(HttpRequest request, OutputStream out) throws IOException {
        Path target = urlToFile(request.getUrl());
        String[] tokens = target.toString().split(Constants.DOT);
        ContentType contentType = ContentType.getContentType(tokens[tokens.length - 1].toUpperCase());
        File file = target.toFile();
        byte[] body = Files.readAllBytes(file.toPath());

        log.debug("staticController handle request ContentType : {}, ContentLength : {}", contentType.getExtension(), body.length);
        return new HttpResponse.Builder(out)
                .setBody(body)
                .setContentType(contentType.getExtension())
                .setContentLength(body.length)
                .setRedirect("./webapp/index.html")
                .build();
    }

    private Path urlToFile(String url) {
        if (url.equals("/")) {
            return Path.of("./webapp/index.html");
        }
        return Path.of("./webapp", url);
    }
}
