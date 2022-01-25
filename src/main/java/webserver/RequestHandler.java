package webserver;

import http.Headers;
import http.Request;
import http.Response;
import http.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Map<Route, Function<Request, Response>> routes;

    public RequestHandler(Socket connectionSocket, Map<Route, Function<Request, Response>> routes) {
        this.connection = connectionSocket;
        this.routes = routes;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = Request.parse(in);
            Response response = handleRequest(request);
            sendResponse(out, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Response handleRequest(Request request) throws IOException {
        for (Map.Entry<Route, Function<Request, Response>> route : routes.entrySet()) {
            if (route.getKey().matches(request)) {
                return route.getValue().apply(request);
            }
        }
        return Response.notFound(Headers.contentType("text/plain"), "Not Found");
    }

    private void sendResponse(OutputStream os, Response response) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeBytes("HTTP/1.1 " + response.getStatusCode() + " " + response.getStatusMessage() + " \r\n");
        for (Map.Entry<String, String> header : response.getHeaders()) {
            dos.writeBytes(header.getKey() + ": " + header.getValue() + "\r\n");
        }
        byte[] body = response.getBody().getBytes(StandardCharsets.UTF_8);
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
