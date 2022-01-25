package handler;

import http.ContentType;
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
            return Response.notFound(Headers.contentType(ContentType.TEXT), "Not Found");
        }
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        ContentType contentType = ContentType.fromExtension(extension);
        try {
            byte[] content = Files.readAllBytes(file.toPath());
            return Response.ok(Headers.contentType(contentType), new String(content));
        } catch (IOException e) {
            return Response.error(Headers.contentType(ContentType.TEXT), e.getMessage());
        }
    }
}
