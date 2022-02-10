package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import util.RedirectPair;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String httpRequestHeader = IOUtils.getHttpRequestHeader(br);
            RedirectPair responseUrlPair = HttpRequestUtils.runMethod(
                    httpRequestHeader, IOUtils.gttHttpRequestBody(br, IOUtils.getContentLength(httpRequestHeader))
            );

            log.info("HTTP Request Header Lines : {}", httpRequestHeader);
            log.info("Response url Path : {}", responseUrlPair.getUrl());

            createResponse(new DataOutputStream(out),responseUrlPair.getUrl() ,responseUrlPair.isRedirect());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void createResponse(DataOutputStream dos, String url,boolean isRedirect) throws IOException {
        String contentType = IOUtils.getContentType(new File("./webapp" + url));
        log.info("contentType : {}", contentType);

        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        if(isRedirect)
            response302Header(dos,contentType,url,body.length);
        else
            response200Header(dos, contentType, body.length);
        responseBody(dos, body);
    }

    private void response302Header(DataOutputStream dos, String contentType, String url, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location : " + url + "\r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
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
