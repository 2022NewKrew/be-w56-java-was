package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HandleRequestHeader;
import response.HandleResponseBody;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            HandleRequestHeader handleRequestHeader = new HandleRequestHeader();

            handleRequestHeader.parseRequestHeader(br);

            HandleResponseBody handleResponseBody = new HandleResponseBody();
            handleResponseBody.setBodyResource(handleRequestHeader.getUrl());

            DataOutputStream dos = new DataOutputStream(out);
            handleResponseBody.response200Header(dos, handleRequestHeader.getFirstAccept());
            handleResponseBody.responseBody(dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
