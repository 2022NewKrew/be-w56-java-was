package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.context.Request;
import webserver.context.RequestFactory;

import java.io.*;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private Dispatcher dispatcher;

    public RequestHandler(Socket connectionSocket, Dispatcher dispatcher) {
        this.connection = connectionSocket;
        this.dispatcher = dispatcher;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            Request request = RequestFactory.createRequestBy(br);

            dispatcher.dispatch(request, out);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
