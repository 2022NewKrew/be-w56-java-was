package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.http.HttpRequest;
import app.http.HttpResponse;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            HttpRequest httpRequest = HttpRequestUtils.parseInput(in);
            HttpResponse httpResponse = HttpResponse.of();
            DispatcherServlet.getInstance().doService(httpRequest, httpResponse);
            if(httpResponse != null) {
                sendResponse(dos, httpResponse);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendResponse(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.getStatusLine());
            dos.writeBytes(httpResponse.header());
            dos.write(httpResponse.getBody(), 0, httpResponse.getBodyLength());
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
