package webserver;

import http.Request;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
            Request request = Request.parse(in);
            Response response = handleRequest(request);
            sendResponse(out, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Response handleRequest(Request request) throws IOException {
        if (request.getMethod().equals("GET")) {
            return handleGetStaticRequest(request);
        }
        return Response.notFound("Not Found");
    }

    private Response handleGetStaticRequest(Request request) throws IOException {
        String name = request.getPath().replaceAll("^/", "");
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream(name);
        if (is == null) {
            return Response.notFound("Not Found");
        }
        byte[] content = is.readAllBytes();
        return Response.ok(new String(content));
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
        os.write(body, 0, body.length);
        os.flush();
    }
}
