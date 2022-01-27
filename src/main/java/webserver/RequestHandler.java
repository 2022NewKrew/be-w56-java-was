package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

import dto.UserSignUpDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.request.HttpRequestFactory;
import service.UserService;
import util.Constants;
import util.ContentType;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            HttpRequest httpRequest = HttpRequestFactory.getInstance().createHttpRequest(br);

            log.debug("request line : {}", httpRequest.getHttpRequestLine());
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.


            // mapper 로 각각의 상황 분리 후 controller 로 전달할 예정
            if (httpRequest.getUrl().contains("/user/create") && Objects.equals(httpRequest.getMethod(), "GET")) {
                UserService userService = new UserService();
                String queries = httpRequest.getUrl().split(Constants.QUESTION)[1];
                Map<String, String> queriesMap = HttpRequestUtils.parseQueryString(queries);
                userService.join(new UserSignUpDto(queriesMap.get("userId"), queriesMap.get("password"), queriesMap.get("name"), queriesMap.get("email")));
            }

            DataOutputStream dos = new DataOutputStream(out);

            byte[] body = Files.readAllBytes(new File(Constants.BASE_FILE_PATH + httpRequest.getUrl()).toPath());

            String extension = HttpRequestUtils.getContentTypeFromUrl(httpRequest.getUrl());
            ContentType contentType = ContentType.getContentType(extension);

            log.debug("extension : {}", extension);
            response200Header(dos, body.length, contentType.getExtension());
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String extension) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + extension + "; charset=utf-8\r\n");
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
