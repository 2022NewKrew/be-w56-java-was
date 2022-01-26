package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Optional;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpHeaderUtils;
import util.IOUtils;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String request = br.readLine();
            log.info("Request line = {}", request);
            String urlWithQuery = HttpHeaderUtils.getHttpRequestUrl(request);
            log.info("url = {}", urlWithQuery);
            String url = HttpHeaderUtils.getUrl(urlWithQuery);

            String line = br.readLine();
            int contentLength = 0;
            while(line != null && !"".equals(line)) {
                log.info("Http header = {}", line);
                String[] token = line.split(": ");
                if(token[0].equals("Content-Length")) {
                    contentLength = Integer.parseInt(token[1]);
                }
                line = br.readLine();
            }

            if(url.equals("/user/create")) {
                String requestBody = IOUtils.readData(br, contentLength);
                log.info("requestBody = {}", requestBody);
                if (requestBody.length() > 0) {
                    User user = HttpHeaderUtils.getUserInfoFromUrl(requestBody);
                    log.info("user = {}", user);
                }
            }

            DataOutputStream dos = new DataOutputStream(out);
            if(new File("./webapp" + url).exists()) {
                byte[] responseBody = Files.readAllBytes(new File("./webapp" + url).toPath());
                response200Header(dos, responseBody.length, HttpHeaderUtils.getContentTypeFromUrl(url));
                responseBody(dos, responseBody);
                return;
            }
            final String redirectUrl = "/index.html";
            response302Header(dos, redirectUrl, HttpHeaderUtils.getContentTypeFromUrl(redirectUrl));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String redirectUrl, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: 0\r\n");
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
