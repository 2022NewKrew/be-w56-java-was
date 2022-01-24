package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import lombok.extern.slf4j.Slf4j;
import model.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            RequestHeader requestHeader = new RequestHeader();
            HttpRequestUtils.setRequest(requestHeader, bufferedReader.readLine());
            bufferedReader.lines()
                    .takeWhile(header -> header.contains(": "))
                    .forEach(header -> HttpRequestUtils.setHeader(requestHeader, header));

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + requestHeader.getHeader("uri")).toPath());
            response200Header(dos, body.length, requestHeader);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, RequestHeader requestHeader) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+ requestHeader.getAccept() + ";charset=utf-8\r\n");
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
