package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.WebServerException;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        MyHttpResponse response = null;

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); OutputStream out = connection.getOutputStream()) {
            MyHttpRequest request = MyHttpRequest.of(in);
            response = RequestMapper.process(request, out);
        } catch (WebServerException e) {
            response = RequestExceptionHandler.handle(e);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            sendResponse(response);
        }
    }

    private void sendResponse(MyHttpResponse response) {
        if (response != null) {
            response.writeBytes();
            response.flush();
        }
    }
}
