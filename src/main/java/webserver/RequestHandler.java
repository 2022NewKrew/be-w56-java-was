package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             OutputStream out = connection.getOutputStream()) {

            MyHttpRequest myHttpRequest = new MyHttpRequest(br);
            log.info(myHttpRequest.toString());

            DataOutputStream dos = new DataOutputStream(out);
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
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
