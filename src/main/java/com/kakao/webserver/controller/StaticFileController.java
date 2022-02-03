package com.kakao.webserver.controller;

import com.kakao.http.header.ContentLengthHeader;
import com.kakao.http.header.ContentTypeHeader;
import com.kakao.http.header.HttpHeader;
import com.kakao.http.request.HttpMethod;
import com.kakao.http.request.HttpRequest;
import com.kakao.http.request.HttpRoute;
import com.kakao.http.response.HttpResponse;
import com.kakao.http.response.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.kakao.webserver.WebServerConfig.DEFAULT_HTTP_VERSION;

public class StaticFileController implements HttpController {
    private static final Logger logger = LoggerFactory.getLogger(StaticFileController.class);

    @Override
    public boolean isValidRoute(HttpRoute route) {
        return HttpMethod.GET.equals(route.getMethod())
                && buildProperPath(route.getPath()).toFile().exists();
    }

    @Override
    public HttpResponse handleRequest(HttpRequest request) throws IOException {
        Path targetPath = buildProperPath(request.getUrl().getPath());
        return responseFile(targetPath.toFile());
    }

    private Path buildProperPath(String path) {
        if (path.equals("/")) {
            return Path.of("./webapp/index.html");
        }
        return Path.of("./webapp", path);
    }

    private HttpResponse responseFile(File file) throws IOException {
        List<HttpHeader> headers = List.of(
                new ContentTypeHeader(file.getName()),
                new ContentLengthHeader(file.length())
        );
        byte[] body = Files.readAllBytes(file.toPath());
        return new HttpResponse(DEFAULT_HTTP_VERSION, HttpStatus.OK, headers, body);
    }
}
