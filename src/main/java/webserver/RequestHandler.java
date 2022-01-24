package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final ViewResolver viewResolver;

    public RequestHandler(Socket connectionSocket) {
        this(connectionSocket, new ViewResolver());
    }

    public RequestHandler(Socket connection, ViewResolver viewResolver) {
        this.connection = connection;
        this.viewResolver = viewResolver;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String requestLine = br.readLine();
            String requestUrl = requestLine.split(" ")[1];

            viewResolver.render(out, requestUrl);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
