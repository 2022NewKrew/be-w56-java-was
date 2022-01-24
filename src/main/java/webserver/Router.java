package webserver;

import http.header.HttpHeaders;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.status.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Router {
    public static final String WEB_ROOT = "./webapp";

    public static HttpResponse statics(HttpRequest request) throws IOException {
        String url = request.getUrl();
        byte[] body = Files.readAllBytes(new File(WEB_ROOT + url).toPath());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, getStaticContentType(url));
        httpHeaders.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(body.length));

        return new HttpResponse(HttpStatus.OK, httpHeaders, body);
    }
    private static String getStaticContentType(String url) {
        String extension = url.substring(url.lastIndexOf(".") + 1);
        switch (extension) {
            case "html":
                return "text/html;charset=utf-8";
            case "css":
                return "text/css";
            case "ico":
                return "image/avif";
            default:
                return "";
        }
    }
}
