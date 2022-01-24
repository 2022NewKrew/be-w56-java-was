package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            // log 출력
            String requestLine = br.readLine();
            log.debug("request line : {}", requestLine);
//            String line = br.readLine();
//            while (!(line).equals("")) {
//                line = br.readLine();
//                log.debug("header : {}", line);
//            }

            DataOutputStream dos = new DataOutputStream(out);
            String url = splitRequestLine(requestLine);
            byte[] body = "Hello World".getBytes();
            if (!Objects.equals(url, "/")) {
                body = Files.readAllBytes(new File("./webapp" + url).toPath());
            }
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String splitRequestLine(String requestLine) {
        String[] tokens = requestLine.split(" ");
        return tokens[1];
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
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
