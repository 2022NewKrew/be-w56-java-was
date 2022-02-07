package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.domain.Model;
import webserver.domain.Request;
import webserver.domain.Response;
import webserver.resolver.ControllerResolver;
import webserver.resolver.ViewResolver;

import java.io.*;
import java.net.Socket;

public class RequestHandler extends Thread {
    public static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    public static final ControllerResolver controllerResolver = ControllerResolver.getInstance();
    public static final ViewResolver viewResolver = ViewResolver.getInstance();

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Request request = new Request(br);
            Model model = new Model();

            String resultFromController = controllerResolver.resolveRequest(request, model);
            Response response = viewResolver.resolveResponse(resultFromController, request.getCookie(), model);

            responseHeader(dos, response.getHeader());
            responseBody(dos, response.getBody());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void responseHeader(DataOutputStream dos, String header) throws IOException {
        dos.writeBytes(header);
    }

    private void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
