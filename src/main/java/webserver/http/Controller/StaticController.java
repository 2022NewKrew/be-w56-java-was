package webserver.http.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.constant.Parser;
import util.constant.Route;
import webserver.http.request.HttpRequest;
import webserver.http.request.Method;
import webserver.http.response.ContentType;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static util.HttpRequestUtils.urlToFile;

public class StaticController implements HttpController {
    private static final Logger log = LoggerFactory.getLogger(StaticController.class);

    @Override
    public boolean isValidRequest(HttpRequest request) {
        Method method = request.getMethod();
        String url = request.getUrl();
        if (url.contains("user/list")) {
            return false;
        }
        return (method == Method.GET && urlToFile(url).toFile().exists());
    }

    @Override
    public HttpResponse handleRequest(HttpRequest request, OutputStream out) throws IOException {
        Path target = urlToFile(request.getUrl());
        String[] tokens = target.toString().split(Parser.DOT);

        ContentType contentType = ContentType.of(tokens[tokens.length - 1].toUpperCase());
        File file = target.toFile();
        byte[] body = Files.readAllBytes(file.toPath());

        log.debug("staticController handle request ContentType : {}, ContentLength : {}", contentType.getExtension(), body.length);
        return new HttpResponse.Builder(out)
                .setBody(body)
                .setHttpStatus(HttpStatus._200)
                .setContentType(contentType.getExtension())
                .setContentLength(body.length)
                .setRedirect(Route.BASE + Route.INDEX)
                .build();
    }
}
