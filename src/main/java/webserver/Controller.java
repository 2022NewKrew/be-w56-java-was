package webserver;

import webserver.http.HttpStatus;
import webserver.http.MIME;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Controller {
    public static void mapping(MyHttpRequest myHttpRequest, DataOutputStream dos) throws IOException {

        String path = myHttpRequest.uri().getPath();

        mappingStaticRequest(path, dos);

        byte[] body;
        MyHttpResponse myHttpResponse;

        switch (path) {
            case "/":
                body = "Hello World".getBytes();
                myHttpResponse = MyHttpResponse.builder(dos)
                        .body(body)
                        .build();
                myHttpResponse.writeBytes();
                break;
            case "/index.html":
                body = Files.readAllBytes(new File("./webapp" + path).toPath());
                myHttpResponse = MyHttpResponse.builder(dos)
                        .body(body)
                        .build();
                myHttpResponse.writeBytes();
                break;
            default:
                body = "Page Not Found".getBytes();
                myHttpResponse = MyHttpResponse.builder(dos)
                        .status(HttpStatus.NOT_FOUND)
                        .body(body)
                        .build();
                myHttpResponse.writeBytes();
        }
    }

    public static void mappingStaticRequest(String path, DataOutputStream dos) throws IOException {

        List<String> STATIC_RESOURCE_DIR = List.of("/js", "/css", "/fonts", "images", "/favicon.ico");

        boolean checkStaticRequest = STATIC_RESOURCE_DIR.stream()
                .anyMatch(path::startsWith);

        if (checkStaticRequest) {
            byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
            MyHttpResponse myHttpResponse = MyHttpResponse.builder(dos)
                    .contentType(MIME.parse(path))
                    .body(body)
                    .build();
            myHttpResponse.writeBytes();
        }

    }
}
