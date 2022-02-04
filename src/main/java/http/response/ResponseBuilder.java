package http.response;

import http.HttpHeader;
import http.HttpStatusCode;
import http.request.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class ResponseBuilder {

    public static void build(String path, HttpRequest request, HttpResponse response) throws IOException {
        response.setHttpVersion("HTTP/1.1");
        byte[] body = {};
        HttpHeader responseHeader = new HttpHeader();

        if(Objects.isNull(path)){
            response.setStatusCode(HttpStatusCode.NOTFOUND);
            response.setBody(body);
            response.setHeader(responseHeader);
            return;
        }

        if(path.contains("redirect:")){
            response.setStatusCode(HttpStatusCode.REDIRECT);
            String redirectUrl = path.split(":")[1];
            response.setBody(body);
            responseHeader.addHeader("Location: " + redirectUrl);
            responseHeader.addHeader("Content-Length: 0");
            response.setHeader(responseHeader);
            return;
        }

        body = Files.readAllBytes(new File("./webapp" + path).toPath());
        response.setBody(body);
        response.setStatusCode(HttpStatusCode.OK);

        responseHeader.addHeader("Content-Type: text/html;charset=utf-8");
        responseHeader.addHeader("Content-Length: " + body.length);
        response.setHeader(responseHeader);
    }
}
