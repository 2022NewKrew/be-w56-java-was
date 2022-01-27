package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import controller.UserCreateController;
import controller.ViewController;
import controller.WebController;
import controller.request.Request;
import controller.request.RequestBody;
import controller.request.RequestHeader;
import controller.request.RequestLine;
import controller.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContentType;
import util.HttpResponseUtils;
import util.HttpStatus;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Map<String, WebController> controllerMap = Maps.newHashMap();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        controllerMap.put("/user/create", new UserCreateController());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // Request
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String requestLineString = br.readLine();
            if (requestLineString == null) {
                return;
            }
            log.debug("request line : {}", requestLineString);
            RequestLine requestLine = RequestLine.from(requestLineString);

            List<String> requestHeaderStrings = new ArrayList<>();
            String line = br.readLine();
            while (!line.isBlank()) {
                requestHeaderStrings.add(line);
                line = br.readLine();
            }
            log.debug("request header : {}", requestHeaderStrings);
            RequestHeader requestHeader = RequestHeader.from(requestHeaderStrings);

            String requestBodyString = null;
            if (requestHeader.getParameter("Content-Length") != null) {
                requestBodyString = IOUtils.readData(br,
                        Integer.parseInt(requestHeader.getParameter("Content-Length")));
            }
            log.debug("request body : {}", requestBodyString);
            RequestBody requestBody = RequestBody.from(requestBodyString);

            Request request = new Request(requestLine, requestHeader, requestBody);
            log.debug("request path : {}", request.getPath());
            WebController webController = controllerMap.getOrDefault(request.getPath(), new ViewController());

            String contentType = ContentType.of(request.getContentType()).getContentType();
            log.debug("request content type : {}", contentType);

            // Response
            Response response = webController.process(request);
            DataOutputStream dos = new DataOutputStream(out);

            dos.writeBytes(HttpResponseUtils.createResponseString(response));

            if (response.getHttpStatus().equals(HttpStatus.OK)) {
                responseBody(dos, response.getResponseBody());
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
