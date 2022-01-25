package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import http.Request;
import http.Response;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            Request request = HttpRequestUtils.parseRequest(bufferedReader);
            Response response = handleRequest(request);

            sendResponse(dos, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Response handleRequest(Request request) {
        if (!request.getMethod().equals("GET")) {
            return Response.builder()
                .statusCode("405")
                .statusText("Method Not Allowed")
                .body(new byte[0])
                .build();
        }
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + request.getTarget()).toPath());
            return Response.builder()
                .statusCode("200")
                .statusText("OK")
                .contextType(request.getHeader("Accept").split(",")[0])
                .body(body)
                .build();
        } catch (IOException e) {
            log.error(e.getMessage());
            return Response.builder()
                .statusCode("404")
                .statusText("Not Found")
                .body(new byte[0])
                .build();
        }
    }

    private void sendResponse(DataOutputStream dos, Response response) {
        try {
            dos.writeBytes("HTTP/1.1 " + response.getHttpStatus() + "\r\n");
            dos.writeBytes("Content-Type: " + response.getContextType() + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + response.getBodyLength() + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(response.getBody(), 0, response.getBodyLength());
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
