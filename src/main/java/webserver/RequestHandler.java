package webserver;

import java.io.*;
import java.net.Socket;

import controller.Controller;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final RequestMapping REQUEST_MAPPING = new RequestMapping();

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

            final RequestLine requestLine = RequestLine.from(IOUtils.readRequestLine(br));
            final RequestHeaders requestHeaders = RequestHeaders.from(IOUtils.readRequestHeaders(br));
            final RequestBodies requestBodies = RequestBodies.of(br, requestHeaders);

//            log.debug("RequestLine : {}", requestLine);
//            log.debug("RequestHeaders : {}", requestHeaders);
//            log.debug("RequestBody : {}", requestBodies);

            Request request = Request.of(requestLine, requestHeaders, requestBodies);

            Response response = convertRequestToResponse(request);

            out.write(response.getHeaderMessage().getBytes());

            byte[] responseBody = response.getResponseBody();
            if (responseBody != null) {
                out.write(response.getResponseBody());
            }

            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Response convertRequestToResponse(Request request) throws IOException {
        Response response = new Response();

        Controller controller = REQUEST_MAPPING.getController(request);
        controller.run(request, response);
        return response;
    }
}
