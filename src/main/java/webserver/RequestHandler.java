package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.DefaultHttpRequestBuilder;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.servlet.HttpHandleable;
import webserver.servlet.HttpHandler;
import webserver.util.HttpResponseUtil;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final HttpHandleable httpRequestServlet = HttpHandler.getInstance();
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            HttpRequest request = new DefaultHttpRequestBuilder(reader).build();
            HttpResponse response = new HttpResponse();

            HttpResponse handledResponse = httpRequestServlet.handle(request, response);
            respond(out, handledResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void respond(OutputStream out, HttpResponse response) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes(HttpResponseUtil.responseLineString(response));
            dos.writeBytes(HttpResponseUtil.headerString(response));
            if (response.getBody() != null && response.getBody().length > 0) {
                dos.write(HttpResponseUtil.bodyString(response).getBytes(StandardCharsets.UTF_8));
            }
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
