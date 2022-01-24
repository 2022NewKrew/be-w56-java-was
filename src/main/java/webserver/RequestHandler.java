package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import model.Request;
import model.RequestHeaders;
import model.RequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final String BASIC_FILE_PATH = "./webapp";
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

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            final RequestLine requestLine = RequestLine.from(IOUtils.readRequestLine(br));
            final RequestHeaders requestHeaders = RequestHeaders.from(IOUtils.readRequestHeaders(br));
            Request request = Request.of(requestLine, requestHeaders);

            final String path = BASIC_FILE_PATH + request.getRequestLine().getUrl();
            log.debug("Request Line Path : {}", path);

            DataOutputStream dos = new DataOutputStream(out);

            byte[] body = Files.readAllBytes(new File(path).toPath());
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
