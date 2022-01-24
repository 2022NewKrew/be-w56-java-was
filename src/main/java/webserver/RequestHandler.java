package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import webserver.controller.StaticController;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final ControllerMapping controllerMapping = new ControllerMapping();

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RequestMap requestMap = readRequestHeader(in);
            createResponse(requestMap, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private RequestMap readRequestHeader(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        RequestMap requestMap = new RequestMap();

        String line = br.readLine();
        log.info("request line {}: ", line);
        String[] requestLine = line.split(" ");
        requestMap.addHeader("url", requestLine[1]);

        while(!line.equals("")){
            line = br.readLine();
            log.debug("request header {}: ", line);
        }

        return requestMap;
    }

    private void createResponse(RequestMap requestMap, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        String url = (String) requestMap.getHeader("url").orElseThrow();
        Controller controller = controllerMapping.getController(url).orElseThrow();
        controller.handle(requestMap, dos);
    }
}
