package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            String buffer;
            buffer = bufferedReader.readLine();

            Request request = Request.of(buffer);

            while ((buffer = bufferedReader.readLine()) != null && !buffer.isBlank()) {
                log.info(buffer);
            }

            Response response = handleRequest(request);

            sendResponse(dos, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Response handleRequest(Request request) {
        if (!request.getMethod().equals("GET")) {
            return Response.from("405", "Method Not Allowed", new byte[0]);
        }
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + request.getTarget()).toPath());
            return Response.from("200", "OK", body);
        } catch (IOException e) {
            log.error(e.getMessage());
            return Response.from("404", "Not Found", new byte[0]);
        }
    }

    private void sendResponse(DataOutputStream dos, Response response) {
        try {
            dos.writeBytes("HTTP/1.1 " + response.getHttpStatus() + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + response.getBodyLength() + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(response.getBody(), 0, response.getBodyLength());
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
