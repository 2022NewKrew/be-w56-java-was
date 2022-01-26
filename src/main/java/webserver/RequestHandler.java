package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
import enums.HttpStatus;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.LogUtils;
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
            LogUtils.requestLog(requestMap, log);

            Map<String,String> headerMap = HttpRequestUtils.readHeader(br);
            LogUtils.headerLog(headerMap, log);

            String url = requestMap.get("httpUrl");
            String cookie = null;
            HttpStatus httpStatus = HttpStatus.OK;
            Map<String, String> params = new HashMap<>();
            if(requestMap.get("httpMethod").equals("POST")) {
                int contentLength = Integer.parseInt(headerMap.get("Content-Length"));
                params = HttpRequestUtils.parseRequestBody(br, contentLength);
            }

            if(url.equals("/user/create")) {
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                log.debug("User : {}", user);
                DataBase.addUser(user);
                httpStatus = HttpStatus.FOUND;
                url = "/index.html";
            }

            if(url.equals("/user/login")) {
                String userId = params.get("userId");
                String password = params.get("password");
                log.debug("userId : {}, password : {}", userId, password);
                User user = DataBase.findUserById(userId);
                cookie = LoginUtils.checkLogin(log, user, password);
                httpStatus = HttpStatus.FOUND;
                url = "/index.html";
            }

            log.debug("\n");

            ResponseHandler responseHandler = new ResponseHandler(out, log);
            responseHandler.response(url, httpStatus, cookie);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
