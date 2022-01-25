package webserver;

import Controller.StaticController;
import Controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import static util.HttpRequestUtils.parseRequestLine;
import static util.HttpRequestUtils.readHeader;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            Map<String, String> requestLine = parseRequestLine(br);
            Map<String, String> requestHeader = readHeader(br);
            DataOutputStream dos = new DataOutputStream(out);
            String url = requestLine.get("url");
            // Static File
            if (url.matches(".+\\.(html|css|js|woff|ttf|ico)$")) {
                StaticController.view(url, dos, requestHeader);
                return;
            }
            String[] path = url.split("/");
            // User
            if (path[1].equals("user")) {
                String userUrl = url.split("/user")[1];
                UserController.view(dos, requestLine, requestHeader, userUrl);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
