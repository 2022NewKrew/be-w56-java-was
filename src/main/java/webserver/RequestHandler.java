package webserver;

import http.Method;
import http.Request;
import http.Response;
import http.Status;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class RequestHandler extends Thread {

    private static final int REQUEST_METHOD_INDEX = 0;
    private static final int REQUEST_TARGET_INDEX = 1;
    private static final int REQUEST_VERSION_INDEX = 2;

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (BufferedReader br =
            new BufferedReader(new InputStreamReader(connection.getInputStream()));
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {

            Request request = parseRequest(br);
            Response response = handleRequest(request);

            sendResponse(dos, request, response);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error("{}", e);
        }
    }

    private Request parseRequest(BufferedReader br) throws IOException {
        String buffer = br.readLine();
        String[] requestTokens = HttpRequestUtils.parseRequestLine(buffer);

        Map<String, String> headers = new HashMap<>();
        while ((buffer = br.readLine()) != null && !buffer.isBlank()) {
            Pair header = HttpRequestUtils.parseHeader(buffer);
            headers.put(header.getKey(), header.getValue());
        }

        return Request.builder()
            .method(Method.valueOf(requestTokens[REQUEST_METHOD_INDEX]))
            .target(requestTokens[REQUEST_TARGET_INDEX])
            .version(requestTokens[REQUEST_VERSION_INDEX])
            .headers(headers)
            .build();
    }

    private Response handleRequest(Request request) {
        if (request.isGet()) {
            return doGet(request);
        }
        if (request.isPost()) {
            return doPost(request);
        }
        if (request.isPut()) {
            return doPut(request);
        }
        if (request.isDelete()) {
            return doDelete(request);
        }
        return Response.builder()
            .status(Status.BAD_REQUEST)
            .build();
    }

    private Response doGet(Request request) {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + request.getTarget()).toPath());
            return Response.builder()
                .status(Status.OK)
                .body(body)
                .build();
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error("{}", e);
            return Response.builder()
                .status(Status.NOT_FOUND)
                .build();
        }
    }

    private Response doPost(Request request) {
        return notAllowedMethod();
    }

    private Response doPut(Request request) {
        return notAllowedMethod();
    }

    private Response doDelete(Request request) {
        return notAllowedMethod();
    }

    private Response notAllowedMethod() {
        return Response.builder()
            .status(Status.METHOD_NOT_ALLOWED)
            .build();
    }

    private void sendResponse(DataOutputStream dos, Request request, Response response) {
        try {
            dos.writeBytes("HTTP/1.1 " + response.getHttpStatus() + System.lineSeparator());
            dos.writeBytes("Content-Type: " +
                HttpRequestUtils.parseAcceptContextType(request.getHeader("Accept"))
                + ";charset=utf-8" + System.lineSeparator());
            dos.writeBytes("Content-Length: " + response.getBodyLength() + System.lineSeparator());
            dos.writeBytes(System.lineSeparator());
            dos.write(response.getBody(), 0, response.getBodyLength());
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
