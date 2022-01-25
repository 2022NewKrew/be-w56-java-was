package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.HttpRequestUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;


public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final UserService userService;

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.userService = UserService.INSTANCE;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String message = br.readLine();
            byte[] body = new byte[0];
            DataOutputStream dos = new DataOutputStream(out);
            String targetResource = null;

            while (message != null && !"".equals(message)) {
                String[] tokens = message.split(" ");

                if (tokens[0].equals("GET")) {
                    targetResource = tokens[1];
                    if (targetResource.startsWith("/user/create")) {
                        signUp(targetResource.split("\\?")[1]);
                        targetResource = targetResource.split("\\?")[0];
                    }
                    body = getBody(targetResource);
                }
                if (tokens[0].equals("Accept:")) {
                    String contentType = tokens[1].split(",")[0];
                    response200Header(dos, body.length, contentType);
                    responseBody(dos, body);
                    log.info("GET " + targetResource + " 200");
                }
                message = br.readLine();
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        String contentTypeMessage = "Content-Type: " + contentType + ";charset=utf-8\r\n";
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes(contentTypeMessage);
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

    private void signUp(String signUpInfo) {
        Map<String, String> userInfo = HttpRequestUtils.parseQueryString(signUpInfo);
        User newUser = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
        userService.addUser(newUser);
    }

    private byte[] getBody(String targetResource) {
        byte[] body;
        try {
            body = Files.readAllBytes(new File("./webapp" + targetResource).toPath());
        } catch (IOException e) {
            body = "Hello World".getBytes();
        }
        return body;
    }

}
