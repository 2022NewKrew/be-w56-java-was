package webserver;

import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Controller {
    public static void mapping(MyHttpRequest myHttpRequest, DataOutputStream dos) throws IOException {

        byte[] body;

        switch (myHttpRequest.uri().getPath()) {
            case "/":
                body = "Hello World".getBytes();
                MyHttpResponse myHttpResponse = MyHttpResponse.builder(dos)
                        .body(body)
                        .build();
                myHttpResponse.writeBytes();
                break;
            case "/favicon.ico":
                body = Files.readAllBytes(new File("./webapp/favicon.ico").toPath());
                MyHttpResponse myHttpResponse1 = MyHttpResponse.builder(dos)
                        .contentType("image/x-icon")
                        .body(body)
                        .build();
                myHttpResponse1.writeBytes();
                break;
        }
    }
}
