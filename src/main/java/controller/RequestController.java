package controller;

import db.DataBase;
import model.RequestHeader;
import model.ResponseHeader;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;

public class RequestController {
    private static final String RETURN_BASE = "./webapp";
    private static final Logger log = LoggerFactory.getLogger(RequestController.class);

    public static ResponseHeader controlRequest(RequestHeader requestHeader){
        ResponseHeader responseHeader = new ResponseHeader();
        String uri = requestHeader.getHeader("uri");
        String method = requestHeader.getHeader("method");
        log.info("CONTROL URI: " + uri);
        log.info("CONTROL METHOD: " + method);

        responseHeader.setStatusCode(200);
        responseHeader.setAccept(requestHeader.getAccept());
        responseHeader.setHost(requestHeader.getHeader("Host"));

        if(uri.equals("/user/create")
                && method.equals("POST")){
            signup(requestHeader);
            responseHeader.setUri("/index.html");
            responseHeader.setStatusCode(302);
            return responseHeader;
        }

        if(!uri.contains(".")){
            uri = uri + ".html";
        }
        responseHeader.setUri(uri);
        return responseHeader;
    }

    private static void signup(RequestHeader requestHeader){
        try{
            log.info("signup");
            String userid = requestHeader.getParameter("userId");
            String password = requestHeader.getParameter("password");
            String name = requestHeader.getParameter("name");
            String email = requestHeader.getParameter("email");
            User user = new User(userid, password, name, email);
            DataBase.addUser(user);
            log.info("SIGNUP OK: " + DataBase.findUserById(userid));
        }catch(NoSuchElementException e){
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }
}
