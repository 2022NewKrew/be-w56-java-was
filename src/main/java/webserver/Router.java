package webserver;

import controller.Controller;
import http.header.HttpHeaders;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.status.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Router {
    public static final String WEB_ROOT = "./webapp";

    public static HttpResponse route(HttpRequest request) throws IOException {
        String url;
        switch (request.getUrl()) {
            case "/":
                url = Controller.index(request);
                break;
            case "/create":
                url = Controller.createUser(request);
                break;
            default:
                url = request.getUrl();
                break;
        }

        byte[] body = Files.readAllBytes(new File(WEB_ROOT + url).toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, getContentType(url));
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(body.length));

        return new HttpResponse(HttpStatus.OK, headers, body);
    }

    private static String getContentType(String url) {
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
