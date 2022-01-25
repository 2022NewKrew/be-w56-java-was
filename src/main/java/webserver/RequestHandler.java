package webserver;

import java.io.*;
import java.net.Socket;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.model.HttpRequestHeader;
import util.HttpRequestHeaderUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final StringBuffer sb = new StringBuffer();

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String request = buffer.readLine();
            HttpRequestHeader httpRequestHeader = HttpRequestHeaderUtils.parseRequestHeader(request);
            DataOutputStream dos = new DataOutputStream(out);
            Path path = new File("./webapp" + httpRequestHeader.getRequestURI()).toPath();
            sb.append("[" + httpRequestHeader.getMethod() + "] " + httpRequestHeader.getRequestURI());
            sb.append(System.getProperty("line.separator"));
            sb.append("path: ");
            sb.append(path);
            sb.append(System.getProperty("line.separator"));
            sb.append("Mime Type: " + httpRequestHeader.getMimeType());
            log.info(sb.toString());
            sb.setLength(0);
            byte[] body = Files.readAllBytes(path);

            log.info("path: " + path);
            response200Header(dos, body.length, httpRequestHeader.getMimeType());
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String mimeType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + mimeType + ";charset=utf-8\r\n");
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
