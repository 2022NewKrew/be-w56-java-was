package webserver;

import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import Router.Router;
import util.HttpRequestUtils;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;


public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            List<String> requestLine = HttpRequestUtils.readRequestMainHeader(br);
            log.debug("request line : {}", requestLine);
            Map<String, String> header = HttpRequestUtils.readHeader(br);
            log.debug("header : {}", header);
            Map<String, String> body = HttpRequestUtils.readBody(br, requestLine, header);
            log.debug("body : {}", body);

            Request request = Request.of(requestLine, header, body);

            Response response = Router.routing(request);

            ResponseHandler responseHandler = new ResponseHandler();
            responseHandler.response(out, response);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
