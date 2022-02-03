package webserver;

import mapper.ResponseSendDataModel;
import mapper.UrlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final UrlMapper urlMapper = new UrlMapper();

    public void sendResponseMessage(Socket socket){
        log.debug("New Client Connect! Connected IP : {}, Port : {}", socket.getInetAddress(),
                socket.getPort());

        try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
            BufferedReader br = new BufferedReader( new InputStreamReader(in, StandardCharsets.UTF_8) );

            HttpRequest httpRequest = HttpRequest.makeHttpRequest(br);

            ResponseSendDataModel result = urlMapper.mappingResult(httpRequest);
            String filename = result.getName();

            String cookie = result.makeCookieString();

            DataOutputStream dos = new DataOutputStream(out);

            if(filename.matches("redirect:.*")){
                response302Header(dos, filename.substring(9), cookie);
                return;
            }

            byte[] responseBody = Files.readAllBytes(new File("./webapp" + filename).toPath());


            response200Header(dos, responseBody.length, cookie);
            responseBody(dos, responseBody);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            if(!"".equals(cookie))
                dos.writeBytes(cookie + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            if(!"".equals(cookie))
                dos.writeBytes(cookie + "\r\n");
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
