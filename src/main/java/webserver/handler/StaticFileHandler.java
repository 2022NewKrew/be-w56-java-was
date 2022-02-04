package webserver.handler;

import http.HttpBody;
import http.HttpHeaders;
import http.HttpStatus;
import http.MultiValueMap;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.StatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.util.HttpResponseUtils;
import webserver.processor.HttpProcessor;
import webserver.resource.StaticFile;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticFileHandler {

    private static final Logger log = LoggerFactory.getLogger(StaticFileHandler.class);

    private static final String PAGE_ROOT = "./webapp";

    public static HttpResponse handle(HttpRequest httpRequest) {

        HttpResponse.Builder builder = HttpResponse.Builder.newInstance();

        HttpStatus httpStatus = HttpStatus.OK;
        MultiValueMap<String, String> headers = new MultiValueMap<>();
        byte[] httpBody = new byte[0];

        try{
            StaticFile staticFile = StaticFile.create(PAGE_ROOT, httpRequest.getUrl());
            headers.add(HttpHeaders.CONTENT_TYPE, HttpResponseUtils.getContentsType(staticFile.getExtension()) + ";charset=utf-8");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(staticFile.getContentsLength()));
            httpBody = staticFile.getContents();
        } catch (NoSuchFileException e) {
            log.info("{} : {}", HttpProcessor.class.getName(), e.getMessage());
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (IOException e) {
            log.info("{} : {}", HttpProcessor.class.getName(), e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return builder
                .statusLine(StatusLine.of(httpRequest.getHttpVersion(), httpStatus))
                .headers(HttpHeaders.of(headers))
                .body(HttpBody.of(httpBody))
                .build();
    }
}
