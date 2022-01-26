package controller;

import http.HttpStatus;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpResponseBody;
import http.response.HttpResponseHeader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public interface Controller {
    HttpResponse process(HttpRequest request) throws IOException;

    default boolean isStaticFileSRequest(HttpRequest request) {
        String url = request.line().url();
        Path path = Path.of("./webapp", url);
        return path.toFile().isFile();
    }

    default HttpResponse readStaticFile(String url) throws IOException {
        HttpResponseBody responseBody = HttpResponseBody.createFromUrl(url);
        HttpResponseHeader responseHeader = new HttpResponseHeader(url, HttpStatus.OK, responseBody.length());

        return new HttpResponse(responseHeader, responseBody);
    }

    default HttpResponse redirect(String redirectUrl) {
        HttpResponseHeader responseHeader = new HttpResponseHeader(redirectUrl, HttpStatus.FOUND, 0);
        responseHeader.putToHeaders("Location", redirectUrl);
        byte[] emptyBody = "".getBytes(StandardCharsets.UTF_8);
        HttpResponseBody responseBody = new HttpResponseBody(emptyBody);

        return new HttpResponse(responseHeader, responseBody);
    }
}
