package model;

import db.DataBase;
import lombok.extern.slf4j.Slf4j;
import util.HtmlResponseHeader;
import util.Links;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.NoSuchElementException;

@Slf4j
public class ResponseHeaderBuilder {
    private final RequestHeader requestHeader;
    public static final String RETURN_BASE = "./webapp";


    public ResponseHeaderBuilder(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public ResponseHeader normalRequest(String uri) {
        if (uri.equals("/")) {
            uri = Links.MAIN;
        }

        if (!uri.contains(".")) {
            uri += ".html";
        }

        return ResponseHeader.builder()
                .uri(uri)
                .htmlResponseHeader(HtmlResponseHeader.RESPONSE_200)
                .body(readBody(uri))
                .accept(requestHeader.getAccept())
                .host(requestHeader.getHeader("Host"))
                .build();
    }

    public ResponseHeader postUserCreate() {
        signup(requestHeader);
        return ResponseHeader.builder()
                .uri(Links.MAIN)
                .body(readBody(Links.MAIN))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302)
                .accept(requestHeader.getAccept())
                .host(requestHeader.getHeader("Host"))
                .build();
    }

    public ResponseHeader postUserLogin() {
        String id = requestHeader.getParameter("userId");
        String password = requestHeader.getParameter("password");
        if (DataBase.isExistUserId(id) && DataBase.findUserById(id).getPassword().equals(password)) {
            return successLogin();
        }
        return failLogin();
    }

    private ResponseHeader successLogin() {
        return ResponseHeader.builder()
                .uri(Links.MAIN)
                .body(readBody(Links.MAIN))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302_WITH_LOGIN_COOKIE)
                .accept(requestHeader.getAccept())
                .host(requestHeader.getHeader("Host"))
                .build();
    }

    private ResponseHeader failLogin() {
        return ResponseHeader.builder()
                .uri(Links.LOGIN_FAILED)
                .body(readBody(Links.LOGIN_FAILED))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302)
                .accept(requestHeader.getAccept())
                .host(requestHeader.getHeader("Host"))
                .build();
    }

    private void signup(RequestHeader requestHeader) {
        try {
            log.info("signup");
            DataBase.addUser(makeUser(requestHeader));
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }

    private User makeUser(RequestHeader requestHeader) {
        return User.builder()
                .userId(requestHeader.getParameter("userId"))
                .password(requestHeader.getParameter("password"))
                .name(requestHeader.getParameter("name"))
                .email((requestHeader.getParameter("email")))
                .build();

    }

    private byte[] readBody(String uri) {
        byte[] body = null;
        try {
            body = Files.readAllBytes(new File(RETURN_BASE + uri).toPath());
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        return body;
    }
}
