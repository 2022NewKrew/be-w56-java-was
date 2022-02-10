package controller;

import db.UserRepository;
import domain.HttpController;
import domain.HttpResponse;
import domain.RequestLine;
import model.User;
import util.HttpRequestUtils;
import view.TemplateEngine;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class GetController implements Controller {

    private static final UserRepository userRepository = UserRepository.getInstance();

    @Override
    public void control(DataOutputStream dos, BufferedReader bufferedReader, RequestLine requestLine) throws IOException {
        HttpController httpController = init(bufferedReader, requestLine);
        byte[] body;

        switch (httpController.getRequestPath()) {
            case "/user/list.html":
                List<User> users = userRepository.findAll();

                String cookie = httpController.getCookie("logined");

                if ("true".equals(cookie)) {
                    body = TemplateEngine.resolveUsersTemplate(httpController.getRequestPath(), users);
                    HttpResponse.response200(dos, body, HttpRequestUtils.parseContentType(httpController.getRequestUrl()));
                    break;
                }

                HttpResponse.response302(dos, "/user/login.html");
                break;

            case "/user/logout":
                System.out.println("hi");
                HttpResponse.response302(dos, "/index.html", "logined=; Path=/; Max-Age=0", "loginId=; Path=/; Max-Age=0");
                break;

            case "/":
                body = TemplateEngine.resolveTemplate("index.html");
                HttpResponse.response200(dos, body, HttpRequestUtils.parseContentType(httpController.getRequestUrl()));
                break;

            default:
                body = TemplateEngine.resolveTemplate(httpController.getRequestPath());
                HttpResponse.response200(dos, body, HttpRequestUtils.parseContentType(httpController.getRequestUrl()));
                break;
        }
    }
}
