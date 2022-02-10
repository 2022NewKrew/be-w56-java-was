package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.WebService;
import util.Header;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            HashMap<String, String> parseRequest =  WebService.parseRequest(br);

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = callFunction(dos, parseRequest);
            responseBody(dos, body);
        } catch (Exception e) {
            e.printStackTrace();
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

    private byte[] callFunction(DataOutputStream dos, HashMap<String, String> parameters){

        String function = WebService.extractFunction(parameters.get("URL"));
        String redirectURL = parameters.get("URL");
        byte[] body = WebService.openUrl(redirectURL);
        if (function.equals("create")) {
            User user = WebService.createUser(parameters.get("body"));
            redirectURL = "/index.html";
            body = WebService.openUrl(redirectURL);

        }
        if (function.equals("login")) {
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
            body = WebService.openUrl(redirectURL);
        }
        if (function.equals("list.html")){
            body = WebService.userList().getBytes(StandardCharsets.UTF_8);
        }

        if (parameters.get("method").equals("GET")){

            Header header = Header.HEADER200;
            header.generate200Header(body.length, logined);
            header.generateHeader(dos);
        }
        else{
            Header header = Header.HEADER302;
            header.generate302Header(redirectURL, logined);
            header.generateHeader(dos);
        }

        log.debug("callFunction results, function : {}, redirectURL : {}", function, redirectURL);
        return body;
    }



}
