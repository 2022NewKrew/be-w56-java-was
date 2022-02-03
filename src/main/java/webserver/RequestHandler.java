package webserver;

import static util.RequestParser.*;
import static util.ResponseGenerator.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.Request;
import http.Response;

public class RequestHandler extends Thread {
    private final String ENCODING = "UTF-8";

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                  connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dataOutputStream = new DataOutputStream(out);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, ENCODING));

            Request request = getRequest(bufferedReader);
            Response response = new Response();
            String source = processRequest(request, response);
            byte[] responseBytes = responseToBytes(source, response);

            dataOutputStream.write(responseBytes, 0, responseBytes.length);
            dataOutputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
