package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import Controllers.Controller;
import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final RequestMapper requestMapper = new RequestMapper();

    private Socket connection;


    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            DataOutputStream dos = new DataOutputStream(out);

            Request request = new Request(br);
            Response response = new Response(dos);
            Controller controller = requestMapper.getController(request);
            controller.service(request, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
