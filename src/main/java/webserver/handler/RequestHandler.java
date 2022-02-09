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

        try (InputStream is = connection.getInputStream();
             OutputStream os = connection.getOutputStream()) {

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            DataOutputStream dos = new DataOutputStream(os);

            HttpRequest request = HttpRequestUtils.parseRequest(br);
            HttpResponse response = RequestMapper.process(request);
            ResponseHandler.sendResponse(response, dos);
        } catch (IOException e) {
            log.error("에러: HTTP 응답을 생성하던 중 IOException 이 발생했습니다.");
            e.printStackTrace();
        }
    }
}
