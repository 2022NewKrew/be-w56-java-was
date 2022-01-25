package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyHttpRequest;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.concurrent.Callable;

public class RequestHandler implements Callable<Void> {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public Void call() throws Exception {

        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            MyHttpRequest myRequest = new MyHttpRequest(in);

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + myRequest.getRequestUrl()).toPath());

            response200Header(dos, myRequest.getAccept(), body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    private void response200Header(DataOutputStream dos, String accept, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + accept + ";charset=utf-8\r\n");
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
