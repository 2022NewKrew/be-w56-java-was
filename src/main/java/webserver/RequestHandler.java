package webserver;

import java.io.*;
import java.net.Socket;

import controller.RequestController;
import webserver.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;
import webserver.http.HttpResponse;

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
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            HttpRequest httpRequest = IOUtils.printAllRequestHeadersAndReturnRequestLine(br);
            log.debug("path: {}", httpRequest.getPath());
            log.debug("params: {}", httpRequest.getParameters());

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse httpResponse = RequestController.controlRequest(httpRequest);
            httpResponse.response(dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
