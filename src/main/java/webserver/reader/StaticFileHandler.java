package webserver.reader;

import http.HttpStatus;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.util.HttpResponseUtils;
import webserver.processor.HttpProcessor;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

public class StaticFileHandler {

    private static final Logger log = LoggerFactory.getLogger(StaticFileHandler.class);

    private static final String PAGE_ROOT = "./webapp";

    public static HttpResponse handle(HttpRequest httpRequest) {

        HttpResponse httpResponse = HttpResponse.create();
        httpResponse.updateProtocolVersion(httpRequest.getHttpVersion());

        try{
            StaticFile staticFile = StaticFile.create(PAGE_ROOT, httpRequest.getUrl());
            httpResponse.addHeader("Content-Type", HttpResponseUtils.getContentsType(staticFile.getExtension()) + ";charset=utf-8");
            httpResponse.addHeader("Content-Length", String.valueOf(staticFile.getContentsLength()));
            httpResponse.updateBody(staticFile.getContents());
        } catch (NoSuchFileException e) {
            log.info("{} : {}", HttpProcessor.class.getName(), e.getMessage());
            httpResponse.updateStatus(HttpStatus.NOT_FOUND);
            return httpResponse;
        } catch (IOException e) {
            log.info("{} : {}", HttpProcessor.class.getName(), e.getMessage());
            httpResponse.updateStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return httpResponse;
        }

        httpResponse.updateStatus(HttpStatus.OK);
        return httpResponse;
    }
}
