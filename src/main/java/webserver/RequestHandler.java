package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import network.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpBuilder;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            DataOutputStream dos = new DataOutputStream(out);

            HttpBuilder httpBuilder = new HttpBuilder(bufferedReader, dos);
            HttpRequest httpRequest = httpBuilder.request();
            httpBuilder.response(httpRequest);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
