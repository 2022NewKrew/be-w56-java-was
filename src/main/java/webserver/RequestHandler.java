package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import controller.UserController;
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
import util.HttpStatus;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Map<String, WebController> controllerMap = Maps.newHashMap();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        controllerMap.put("/user/create", new UserController());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
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
            Response response = webController.process(request);

            if (response.getHttpStatus().equals(HttpStatus.OK)) {
                byte[] body = Files.readAllBytes(new File("./webapp" + response.getResponseUrl()).toPath());
                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, contentType, body.length);
                responseBody(dos, body);
            }

            if (response.getHttpStatus().getStatusName().equals(HttpStatus.REDIRECT.getStatusName())) {
                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos, response.getResponseUrl());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/" + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 NOT FOUND \r\n");
            dos.writeBytes("\r\n");
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
