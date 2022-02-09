package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import user.controller.UserCreate;
import user.controller.UserLogin;
import util.HttpRequestUtils;
import util.IOUtils;

import static util.HttpRequestUtils.getUrlExtension;
import static util.HttpRequestUtils.parseRequestLine;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String LOGIN_FAIL_PATH = "user/login_failed.html";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
             DataOutputStream dos = new DataOutputStream(out);
            String requestLine = br.readLine();

            HttpRequestUtils.Pair urlPair = parseRequestLine(requestLine);
            String method = urlPair.getKey();
            String url = urlPair.getValue();
            Map<String, String> headers = getHeaders(br, requestLine);
            if (method.equals("POST") && url.equals("/user/create")) {
                String path = new UserCreate().execute(br, headers);
                response302Header(dos, path);
            } else if (method.equals("POST") && url.equals("/user/login")) {
                Map<String, String> loginResult = new UserLogin().execute(br, headers);
                reponse302HeaderWithCookie(dos, loginResult.get("path"), loginResult.get("cookie"));
            }
            else {
                byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String path) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /" + path + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void reponse302HeaderWithCookie(DataOutputStream dos, String path, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /" + path + "\r\n");
            dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Map<String, String> getHeaders(BufferedReader br, String requestLine) throws IOException{
        Map<String, String> headers = new HashMap<String, String>();
        while(!requestLine.equals("")) {
            if (requestLine == null) {
                break;
            }
            requestLine = br.readLine();
            String[] headerTokens = requestLine.split(": ");
            if (headerTokens.length == 2) {
                headers.put(headerTokens[0], headerTokens[1]);
            }
        }
        return headers;
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