package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.WebService;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private Boolean logined = false;

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // bufferedreader API 활용
            // java inputstream bufferedreader 변환

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            HashMap<String, String> parseRequest =  WebService.parseRequest(br);

            DataOutputStream dos = new DataOutputStream(out);
            String url = callFunction(parseRequest);

            byte[] body = WebService.openUrl(url);
            log.debug("url compare {}, {}", url, parseRequest.get("URL"));
            if (url.equals(parseRequest.get("URL"))){
                response200Header(dos, body.length);
            }
            else{
                response302Header(dos, url);
            }

            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Set-Cookie: logined=" + logined.toString() +"; Path=/"+ "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String redirectURL) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectURL + "\r\n");
            dos.writeBytes("Set-Cookie: logined=" + logined.toString() +"; Path=/"+ "\r\n");
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

    private String callFunction(HashMap<String, String> parameters){

        String method = WebService.extractFunction(parameters.get("URL"));
        String redirectURL = parameters.get("URL");
        if (method.equals("create")) {
            User user = WebService.createUser(parameters.get("body"));
            redirectURL = "/index.html";
        }
        if (method.equals("login")) {
            if (WebService.loginUser(parameters.get("body"))){
                log.debug("login success");
                redirectURL = "/index.html";
                logined = true;
            }
            else{
                log.debug("login failed");
                redirectURL = "/user/login_failed.html";
                logined = false;
            }
        }
        log.debug("callFunction results, method : {}, redirectURL : {}", method , redirectURL);
        return redirectURL;
    }


}
