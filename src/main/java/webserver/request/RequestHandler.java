package webserver.request;

import controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Response;
import webserver.http.request.HttpRequest;
import webserver.request.parser.RequestParser;
import webserver.request.process.ControllerMethodMatcher;
import webserver.response.ResponseHandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
              DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {

            HttpRequest httpRequest = RequestParser.parse(br);
            Response result = ControllerMethodMatcher.match(Controller.class, httpRequest);

            ResponseHandler responseHandler = new ResponseHandler(dos, httpRequest.getHeaderAttribute("VERSION"));
            responseHandler.response(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
