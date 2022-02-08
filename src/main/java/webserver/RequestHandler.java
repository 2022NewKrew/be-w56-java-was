package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import controller.Controller;
import domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Map<String, Controller> controllerMap;

    public RequestHandler(Socket connectionSocket, Map<String, Controller> controllerMap) {
        this.connection = connectionSocket;
        this.controllerMap = controllerMap;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {

            RequestLine requestLine = new RequestLine(bufferedReader.readLine());
            log.info("HTTP Request Line : {}", requestLine);

            Controller controller = controllerMap.get(requestLine.getMethod());
            controller.control(dos, bufferedReader, requestLine);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
