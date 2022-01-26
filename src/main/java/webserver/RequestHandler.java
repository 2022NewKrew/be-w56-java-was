package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.request.Request;
import util.request.RequestUrlMapper;
import util.response.HttpResponseUtils;
import util.response.Response;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final RequestUrlMapper requestUrlMapper = RequestUrlMapper.getRequestUrlMapper();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            Request request = new Request(br);
            DataOutputStream dos = new DataOutputStream(out);
            Response response = requestUrlMapper.mapping(request);
            HttpResponseUtils.httpResponse(dos, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
