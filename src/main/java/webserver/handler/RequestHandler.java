package webserver.handler;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.mapper.RequestMapper;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        HttpResponse response = null;
        BufferedReader br;
        DataOutputStream dos;

        try {
             br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            dos = new DataOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            log.error("에러: HTTP 응답을 생성하던 중 IOException 이 발생했습니다.");
            e.printStackTrace();
            closeSocket(connection);
            return;
        }

        try {
            HttpRequest request = HttpRequestUtils.parseRequest(br);
            response = RequestMapper.process(request);
        } catch (Exception e) {
            log.error(String.format("에러: %s", e.getMessage()));
            e.printStackTrace();
            response = RequestExceptionHandler.handle(e);
        } finally {
            ResponseHandler.sendResponse(response, dos);
            closeSocket(connection);
        }
    }

    private void closeSocket(Socket connection) {
        try {
            connection.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
