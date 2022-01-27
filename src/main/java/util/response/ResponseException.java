package util.response;

import util.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResponseException {
    public static Response notFoundResponse() throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/404.html").toPath());
        return new ResponseBuilder()
                .setHttpStatus(HttpStatus.NOT_FOUND)
                .addHeader("Content-Type","text/html;charset=utf-8")
                .addHeader("Content-length", String.valueOf(body.length))
                .setBody(body)
                .build();
    }

    public static Response badRequestResponse() throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/error.html").toPath());
        return new ResponseBuilder()
                .setHttpStatus(HttpStatus.BAD_REQUEST)
                .addHeader("Content-Type","text/html;charset=utf-8")
                .addHeader("Content-length", String.valueOf(body.length))
                .setBody(body)
                .build();
    }
}
