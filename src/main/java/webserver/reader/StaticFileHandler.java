package webserver.reader;

import http.HttpBody;
import http.HttpHeaders;
import http.HttpStatus;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.StatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.util.HttpResponseUtils;
import webserver.processor.HttpProcessor;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Map;

public class StaticFileHandler {

    private static final Logger log = LoggerFactory.getLogger(StaticFileHandler.class);

    private static final String PAGE_ROOT = "./webapp";

    public static HttpResponse handle(HttpRequest httpRequest) {

        HttpResponse.Builder builder = HttpResponse.Builder.newInstance();

        try{
            StaticFile staticFile = StaticFile.create(PAGE_ROOT, httpRequest.getUrl());
            Map<String, String> headers = new HashMap<>();
            headers.put(HttpHeaders.CONTENT_TYPE, HttpResponseUtils.getContentsType(staticFile.getExtension()) + ";charset=utf-8");
            headers.put(HttpHeaders.CONTENT_LENGTH, String.valueOf(staticFile.getContentsLength()));

            builder = builder.statusLine(StatusLine.of(httpRequest.getHttpVersion(), HttpStatus.OK))
                    .headers(HttpHeaders.of(headers))
                    .body(HttpBody.of(staticFile.getContents()));

        } catch (NoSuchFileException e) {
            log.info("{} : {}", HttpProcessor.class.getName(), e.getMessage());
            return builder
                    .headers(HttpHeaders.of(new HashMap<>()))
                    .statusLine(StatusLine.of(httpRequest.getHttpVersion(), HttpStatus.NOT_FOUND))
                    .body(HttpBody.of(new byte[0]))
                    .build();
        } catch (IOException e) {
            log.info("{} : {}", HttpProcessor.class.getName(), e.getMessage());
            return builder
                    .headers(HttpHeaders.of(new HashMap<>()))
                    .statusLine(StatusLine.of(httpRequest.getHttpVersion(), HttpStatus.INTERNAL_SERVER_ERROR))
                    .body(HttpBody.of(new byte[0]))
                    .build();
        }

        return builder.build();
    }
}
