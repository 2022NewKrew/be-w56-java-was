package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import request.HttpRequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.HttpResponseHeader;
import util.HttpRequestUtils;
import util.HttpResponseUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();
             InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr);
             DataOutputStream dos = new DataOutputStream(out);) {

            String line = br.readLine();
            HttpRequestLine requestLine = HttpRequestUtils.parseRequestLine(line);

            while (!"".equals(line)) {
                log.debug(line);

                line = br.readLine();
            }

            byte[] body = Files.readAllBytes(new File("./webapp" + requestLine.getUrl()).toPath());

            HttpResponseHeader responseHeader = new HttpResponseHeader(HttpResponseUtils.contentTypeOf(requestLine.getUrl()), body.length);
            responseHeader.writeToDataOutputStream(dos);
            //response200Header(dos, HttpResponseUtils.contentTypeOf(requestLine.getUrl()), body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
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
