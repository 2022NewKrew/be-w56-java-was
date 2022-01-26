package webserver;

import db.DB;
import http.request.HttpRequest;
import http.request.HttpRequestDecoder;
import lombok.extern.slf4j.Slf4j;
import model.User;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@Slf4j
public class RequestHandler extends Thread {

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        ) {
            HttpRequest request = HttpRequestDecoder.decode(br);
            Map<String, String> queryParams = request.getQueryParams();
            List<String> userProperties = List.of("userId", "password", "name", "email");

            if (request.getUri().startsWith("/user/create") && userProperties.stream().allMatch(queryParams::containsKey)) {
                User user = User.builder()
                        .userId(queryParams.get("userId"))
                        .password(queryParams.get("password"))
                        .name(queryParams.get("name"))
                        .email(queryParams.get("email"))
                        .build();

                DB.addUser(user);
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + request.getUri()).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /* ---------------------------------------------------------------------- */

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        // TODO: content-type
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
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
