package webserver.handler.handlerAdapter;

import static util.Constant.ACCEPT;
import static util.Constant.ALL_CONTENT_TYPE;
import static util.Constant.COMMA;
import static util.Constant.CONTENT_LENGTH;
import static util.Constant.CONTENT_TYPE;
import static util.Constant.INDEX_PATH;
import static util.Constant.ROOT_PATH;
import static util.Constant.UTF_8;
import static util.Constant.WEBAPP_PATH;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.http.HttpRequest;
import app.http.HttpResponse;
import app.http.HttpStatus;
import app.http.HttpVersion;
import app.http.Mime;
import webserver.handler.HandlerMethod;

public class StaticHandlerAdapter implements HandlerAdapter{
    private static final Logger log = LoggerFactory.getLogger(StaticHandlerAdapter.class);

    @Override
    public boolean supports(Object handlerMethod) {
        return true;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HandlerMethod handlerMethod) {
        setResponse(request, response, request.getUrl());
    }

    private void setResponse(HttpRequest request, HttpResponse response, String path) {
        response.put(CONTENT_TYPE, request.get(ACCEPT, ALL_CONTENT_TYPE).split(COMMA)[0] + UTF_8);
        try {
            if (path.equals(ROOT_PATH)) {
                path = INDEX_PATH;
            }
            path = WEBAPP_PATH + path;
            response.put(CONTENT_TYPE, Mime.getMime(path).getContentType() + UTF_8);
            byte[] body = Files.readAllBytes(new File(path).toPath());
            response.put(CONTENT_LENGTH, String.valueOf(body.length));
            response.setVersion(HttpVersion.HTTP_1_1);
            response.setStatus(HttpStatus.OK);
            response.setBody(body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
