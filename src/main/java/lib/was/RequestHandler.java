package lib.was;

import lib.was.http.ContentType;
import lib.was.http.Headers;
import lib.was.http.Request;
import lib.was.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lib.was.router.RouterFunction;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final List<RouterFunction> routers;

    public RequestHandler(Socket connectionSocket, List<RouterFunction> routers) {
        this.connection = connectionSocket;
        this.routers = routers;
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

    private Response handleRequest(Request request) {
        for (RouterFunction router : routers) {
            Response response = router.handle(request);
            if (response != null) {
                return response;
            }
        }
        return Response.notFound(Headers.contentType(ContentType.TEXT), "Not Found");
    }

    private void sendResponse(OutputStream os, Response response) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeBytes("HTTP/1.1 " + response.getStatusCode() + " " + response.getStatusMessage() + " \r\n");
        for (Map.Entry<String, String> header : response.getHeaders()) {
            dos.writeBytes(header.getKey() + ": " + header.getValue() + "\r\n");
        }
        byte[] body = response.getBody();
        dos.writeBytes("\r\n");
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
