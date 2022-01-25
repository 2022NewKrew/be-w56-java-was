package controller;

import dto.UserCreateRequestDto;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Service;
import webserver.ParsedRequest;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Controller {

    private final Service service = new Service();
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    public void makeResponse(ParsedRequest parsedRequest, DataOutputStream dos) throws IOException {
        String url = parsedRequest.getUrl();
        switch(url) {
            case "/user/create":
                Map<String, String> parsedQueryString = parsedRequest.getParsedQueryString();
                service.signUp(new UserCreateRequestDto(parsedQueryString.get("userId"), parsedQueryString.get("password"), parsedQueryString.get("name"), parsedQueryString.get("email")));
                response302Header(dos);
                break;
            default:
                Path filePath = Paths.get("./webapp" + url);
                File file = new File(filePath.toString());
                String mimeType = new Tika().detect(file);
                byte[] body = Files.readAllBytes(filePath);
                response200Header(dos, mimeType, body.length);
                responseBody(dos, body);
        }
    }

    private void response200Header(DataOutputStream dos, String mimeType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + mimeType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: http://localhost:8080/index.html");
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
