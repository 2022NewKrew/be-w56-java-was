package handler;

import http.Headers;
import http.Request;
import http.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticHandler {

    public Response get(Request request) {
        File file = new File("./webapp/" + request.getPath());
        if (!file.exists()) {
            return Response.notFound(Headers.contentType("text/plain"), "Not Found");
        }
        try {
            byte[] content = Files.readAllBytes(file.toPath());
            String contentType = getContentType(file);
            return Response.ok(Headers.contentType(contentType), new String(content));
        } catch (IOException e) {
            return Response.error(Headers.contentType("text/plain"), e.getMessage());
        }
    }

    private String getContentType(File file) {
        String fileName = file.getName();
        if (fileName.endsWith(".html")) {
            return "text/html";
        }
        if (fileName.endsWith(".css")) {
            return "text/css";
        }
        if (fileName.endsWith(".js")) {
            return "text/javascript";
        }
        return "text/plain";
    }
}
