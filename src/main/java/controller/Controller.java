package controller;

import http.HttpStatus;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpResponseBody;
import http.response.HttpResponseHeader;

import java.io.IOException;
import java.nio.file.Path;

/**
 *  process 가 호출되면 request 객체를 통해
 *  정적 파일에 대한 요청인지 확인
 *  정적 파일 요청이 아니면 processDynamic 로 처리 위임
 */
public interface Controller {
    HttpResponse processDynamic(HttpRequest request) throws IOException;

    default HttpResponse process(HttpRequest request) throws IOException {
        if (isStaticFileRequest(request))
            return readStaticFile(request.line().url());

        return processDynamic(request);
    }

    default boolean isStaticFileRequest(HttpRequest request) {
        String url = request.line().url();
        Path path = Path.of("./webapp", url);
        return path.toFile().isFile();
    }

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

        return new HttpResponse(responseHeader, HttpResponseBody.empty());
    }

    default HttpResponse errorPage() {
        HttpResponseHeader responseHeader = new HttpResponseHeader("/index.html", HttpStatus.BAD_REQUEST, 0);

        return new HttpResponse(responseHeader, HttpResponseBody.empty());
    }
}
