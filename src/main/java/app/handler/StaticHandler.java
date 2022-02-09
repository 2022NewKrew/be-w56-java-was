package app.handler;

import lib.was.di.Bean;
import lib.was.http.ContentType;
import lib.was.http.Request;
import lib.was.http.Response;

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
            return Response.notFound("Not Found").contentType(ContentType.TEXT);
        }
        String filename = file.getName();
        String extension = filename.substring(filename.lastIndexOf("."));
        ContentType contentType = ContentType.fromExtension(extension).orElse(ContentType.TEXT);
        try {
            byte[] content = Files.readAllBytes(file.toPath());
            return Response.ok(content).contentType(contentType);
        } catch (IOException e) {
            return Response.error(e.getMessage()).contentType(ContentType.TEXT);
        }
    }
}
