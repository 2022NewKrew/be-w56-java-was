package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
import model.User;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.LoginUtils;

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

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            Map<String, String> requestMap = HttpRequestUtils.readRequest(br);

            if(requestMap == null)
                throw new IOException("Incorrect Request Header");

            log.debug("request");
            for(Map.Entry<String, String> request : requestMap.entrySet())
                log.debug("{} : {}", request.getKey(), request.getValue());

            Map<String,String> headerMap = HttpRequestUtils.readHeader(br);

            log.debug("header");
            for(Map.Entry<String, String> header : headerMap.entrySet())
                log.debug("{} : {}", header.getKey(), header.getValue());

            String url = requestMap.get("httpUrl");
            String cookie = null;
            int httpStatus = 200;
            Map<String, String> params = new HashMap<>();
            if(requestMap.get("httpMethod").equals("POST")) {
                int contentLength = Integer.parseInt(headerMap.get("Content-Length"));
                params = HttpRequestUtils.parseRequestBody(br, contentLength);

            }

            if(url.equals("/user/create")) {
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                log.debug("User : {}", user);
                DataBase.addUser(user);
                httpStatus = 302;
                url = "/index.html";
            }

            if(url.equals("/user/login")) {
                String userId = params.get("userId");
                String password = params.get("password");
                log.debug("userId : {}, password : {}", userId, password);
                User user = DataBase.findUserById(userId);
                cookie = LoginUtils.checkLogin(log, user, password);
                httpStatus = 302;
                url = "/index.html";
            }

            log.debug("\n");

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            response(dos, url, httpStatus, cookie);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response(DataOutputStream dos, String url, int httpStatus, String cookie) throws IOException {
        switch(httpStatus) {
            case 200:
                File file = new File("./webapp" + url);
                String contentType = new Tika().detect(file);
                byte[] body = Files.readAllBytes(file.toPath());
                response200Header(dos, body.length, contentType);
                responseBody(dos, body);
                break;
            case 302:
                response302(dos, cookie, url);
                break;
        }
    }

    private void response302(DataOutputStream dos, String cookie, String url) {
        if(cookie == null) {
            response302Header(dos, url);
            return;
        }
        response302HeaderWithCookie(dos, cookie, url);
    }

    private void response302HeaderWithCookie(DataOutputStream dos, String cookie, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("Set-Cookie: " + cookie + "; Path=/");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("\r\n");
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
