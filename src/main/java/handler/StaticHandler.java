package handler;

import annotation.Bean;
import http.ContentType;
import http.Headers;
import http.Request;
import http.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Bean
public class StaticHandler {

    public Response get(Request request) {
        String filePath = request.getPath();
        if (filePath.endsWith("/")) {
            filePath += "index.html";
        }
        File file = new File("./webapp" + filePath);
        if (!file.exists()) {
            return Response.notFound(Headers.contentType(ContentType.TEXT), "Not Found");
        }
        String filename = file.getName();
        String extension = filename.substring(filename.lastIndexOf("."));
        ContentType contentType = ContentType.fromExtension(extension).orElse(ContentType.TEXT);
        try {
            byte[] content = Files.readAllBytes(file.toPath());
            return new Response(200, Headers.contentType(contentType), content);
        } catch (IOException e) {
            return Response.error(Headers.contentType(ContentType.TEXT), e.getMessage());
        }
    }
}
