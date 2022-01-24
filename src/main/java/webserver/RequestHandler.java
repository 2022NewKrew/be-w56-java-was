package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;

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
            BufferedReader br = new BufferedReader( new InputStreamReader(in));
            String requestLine = br.readLine();
            log.debug("REQ: {}", requestLine);

            // Request Header 첫 번째 요소 추출
            List<String> requestElement = List.of(requestLine.split(" "));
            String requestMethod = requestElement.get(0);
            String requestUrl = requestElement.get(1);
            String clientHttpVersion = requestElement.get(2);
            log.debug("requestMethod : {}", requestMethod);
            log.debug("requestUrl : {}", requestUrl);
            log.debug("clientHttpVersion : {}", clientHttpVersion);

            // 모든 Request Header 출력
            String line;
            while (!(line = br.readLine()).equals("")) {
                log.debug("other: {}", line);
            }

            // Response 메시지 구성
            DataOutputStream dos = new DataOutputStream(out);
            // 요구한 URL의 html파일로 응답
            byte[] body = Files.readAllBytes(new File("./webapp" + requestUrl).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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
