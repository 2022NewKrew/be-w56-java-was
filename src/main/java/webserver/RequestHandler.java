package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpStatusCode;
import webserver.http.ResponseData;
import webserver.http.request.Request;
import webserver.http.response.Response;

public class RequestHandler extends Thread {
    private static final String ROOT_DIRECTORY = "./webapp";

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = new Request(in);
            request.read();

            Response response = new Response(out, checkDirectory(request));
            response.write();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private ResponseData checkDirectory(Request request){
        if(Files.isRegularFile(Path.of(ROOT_DIRECTORY+ request.getUrl()))){
            log.info("File exist");
            return new ResponseData(HttpStatusCode.SUCCESS, request.getUrl());
        }
        log.info("URL mapping");
        return Controller.proceed(request);
    }
}
