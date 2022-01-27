package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.DefaultHttpRequestBuilder;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.servlet.HttpRequestResponsible;
import webserver.servlet.HttpRequestServlet;
import webserver.util.HttpResponseUtil;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final HttpRequestResponsible controller = HttpRequestServlet.getInstance();
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            HttpRequest request = new DefaultHttpRequestBuilder()
                .init(reader.readLine())
                .headers(reader)
                .build();
            HttpResponse response = new HttpResponse();

            HttpResponse handledResponse = controller.handle(request, response);
            respond(out, handledResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void respond(OutputStream out, HttpResponse response) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes(HttpResponseUtil.write(response));
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
