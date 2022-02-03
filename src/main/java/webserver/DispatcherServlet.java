package webserver;

import lombok.extern.slf4j.Slf4j;
import webserver.model.WebHttpRequest;
import webserver.model.WebHttpResponse;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Slf4j
public class DispatcherServlet extends Thread {
    private final ViewResolver resolver = ViewResolver.getInstance();
    private final RequestHandler requestHandler = RequestHandler.getInstance();
    private final Socket connection;

    public DispatcherServlet(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            DataOutputStream dos = new DataOutputStream(out);

            WebHttpRequest httpRequest = WebHttpRequest.of(buffer);
            WebHttpResponse httpResponse = WebHttpResponse.of(httpRequest);
            log.info(httpRequest.toString());
            requestHandler.handle(httpRequest, httpResponse);
            resolver.resolve(httpRequest, httpResponse, dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
