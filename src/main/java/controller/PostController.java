package controller;

import db.UserRepository;
import domain.*;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class PostController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private static final UserRepository userRepository = UserRepository.getInstance();

    @Override
    public void control(DataOutputStream dos, BufferedReader bufferedReader, RequestLine requestLine) throws IOException {
        HttpController httpController = init(bufferedReader, requestLine);
        HttpBody httpBody = httpController.getHttpBody(bufferedReader);

        switch (httpController.getRequestPath()) {
            case "/user/create":
                User user = new User(httpBody.get("userId"), httpBody.get("password"), httpBody.get("name"), httpBody.get("email"));
                userRepository.addUser(user);
                log.info("FindUser : {}", userRepository.findUserById(httpBody.get("userId")));

                HttpResponse.response302(dos, "/index.html");
                break;

            case "/user/login":
                User findUser = userRepository.findUserById(httpBody.get("userId"));
                String redirectPath = selectLoginRedirectPath(httpBody.get("password"), findUser);
                String cookie = selectLoginCookie(httpBody.get("password"), findUser);

                HttpResponse.response302(dos, redirectPath, cookie);
                break;
        }
    }

    private String selectLoginCookie(String loginPassword, User user) {
        if (user != null && checkPassword(loginPassword, user.getPassword())) {
            return "logined=true; Path=/";
        }
        return "logined=false; Path=/";
    }

    private String selectLoginRedirectPath(String loginPassword, User user) {
        if (user != null && checkPassword(loginPassword, user.getPassword())) {
            return "/index.html";
        }
        return "/user/login_failed.html";
    }

    private boolean checkPassword(String loginPassword, String storedPassword) {
        return loginPassword.equals(storedPassword);
    }
}
