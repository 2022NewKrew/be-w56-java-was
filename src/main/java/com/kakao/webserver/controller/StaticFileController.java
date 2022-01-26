package com.kakao.webserver.controller;

import com.kakao.http.header.ContentLengthHeader;
import com.kakao.http.header.ContentTypeHeader;
import com.kakao.http.header.HttpHeader;
import com.kakao.http.request.HttpMethod;
import com.kakao.http.request.HttpRequest;
import com.kakao.http.response.HttpResponse;
import com.kakao.http.response.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.kakao.webserver.WebServerConfig.DEFAULT_HTTP_VERSION;

public class StaticFileController implements HttpController {
    private static final Logger logger = LoggerFactory.getLogger(StaticFileController.class);

    @Override
    public boolean isValidRequest(String path, HttpMethod method) {
        return buildProperPath(path).toFile()
                .exists();
    }

    @Override
    public void handleRequest(HttpRequest request, OutputStream os) {
        Path targetPath = buildProperPath(request.getUrl().getPath());
        responseFile(os, targetPath.toFile());
    }

    private Path buildProperPath(String path) {
        if (path.equals("/")) {
            return Path.of("./webapp/index.html");
        }
        return Path.of("./webapp", path);
    }

    private void responseFile(OutputStream out, File file) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            List<HttpHeader> headers = List.of(new ContentTypeHeader(file.getName()),
                    new ContentLengthHeader(file.length()));
            HttpResponse httpResponse = new HttpResponse(DEFAULT_HTTP_VERSION, HttpStatus.OK, headers);
            dos.writeBytes(httpResponse.toString());
            responseBody(dos, Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
