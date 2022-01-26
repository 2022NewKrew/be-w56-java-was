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

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); OutputStream out = connection.getOutputStream()) {
            MyHttpRequest request = MyHttpRequest.of(in);

            MyHttpResponse response = RequestMapper.process(request, out);
            response.writeBytes();
            response.flush();
        } catch (WebServerException e) {
            // TODO - 웹서버 예외 처리
            log.error(e.getMessage());
        } catch (Exception e) {
            // TODO - 비정상 예외 처리
            log.error(e.getMessage());
        }
    }
}
