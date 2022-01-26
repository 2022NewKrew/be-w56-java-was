package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import webserver.web.HttpStatus;
import webserver.web.request.Request;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final RequestMapper mapper;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        mapper = RequestMapper.getInstance();
        log.debug("mapper 연결 : {}", mapper);
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = Request.getRequest(in);
            Response response = handle(mapper.mapping(request), request, out);
            //log.debug("{} 결과 : {}", request.getUrl().getUrl(), response.getStatus().valueOf());

            response.send();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private Response handle(Optional<Controller> handler, Request request, OutputStream out) throws IOException{
        if(handler.isEmpty()) {
            return new Response.ResponseBuilder(out).setStatus(HttpStatus.NOT_FOUND).build();
        }
        return handler.get().handle(request, out);
    }
}
