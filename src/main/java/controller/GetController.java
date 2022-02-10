package controller;

import db.MemoRepository;
import db.UserRepository;
import domain.HttpController;
import domain.HttpResponse;
import domain.RequestLine;
import model.Memo;
import model.User;
import util.HttpRequestUtils;
import view.TemplateEngine;

import java.io.*;
import java.util.*;

public class GetController implements Controller {

    private static final UserRepository userRepository = UserRepository.getInstance();
    private static final MemoRepository memoRepository = MemoRepository.getInstance();

    @Override
    public void control(DataOutputStream dos, BufferedReader bufferedReader, RequestLine requestLine) throws IOException {
        HttpController httpController = init(bufferedReader, requestLine);
        byte[] body;

        switch (httpController.getRequestPath()) {
            case "/user/list":
                List<User> users = userRepository.findAll();

                String cookie = httpController.getCookie("logined");

                if ("true".equals(cookie)) {
                    body = TemplateEngine.resolveUsersTemplate(httpController.getRequestPath(), users);
                    HttpResponse.response200(dos, body, HttpRequestUtils.parseContentType(httpController.getRequestUrl()));
                    break;
                }

                HttpResponse.response302(dos, "/user/login");
                break;

            case "/qna/form":
                String logined = httpController.getCookie("logined");

                if ("true".equals(logined)) {
                    body = TemplateEngine.resolveTemplate("/qna/form.html");
                    HttpResponse.response200(dos, body, HttpRequestUtils.parseContentType(httpController.getRequestUrl()));
                    break;
                }

                HttpResponse.response302(dos, "/user/login");
                break;

            case "/user/logout":
                HttpResponse.response302(dos, "/", "logined=; Path=/; Max-Age=0", "loginId=; Path=/; Max-Age=0");
                break;

            case "/user/login":
                body = TemplateEngine.resolveTemplate("/user/login.html");
                HttpResponse.response200(dos, body, HttpRequestUtils.parseContentType(httpController.getRequestUrl()));
                break;

            case "/user/login_failed":
                body = TemplateEngine.resolveTemplate("/user/login_failed.html");
                HttpResponse.response200(dos, body, HttpRequestUtils.parseContentType(httpController.getRequestUrl()));
                break;

            case "/user/form":
                body = TemplateEngine.resolveTemplate("/user/form.html");
                HttpResponse.response200(dos, body, HttpRequestUtils.parseContentType(httpController.getRequestUrl()));
                break;

            case "/":
                List<Memo> memos = memoRepository.findAll();
                body = TemplateEngine.resolveMemosTemplate("/index", memos);
                HttpResponse.response200(dos, body, HttpRequestUtils.parseContentType(httpController.getRequestUrl()));
                break;

            default:
                body = TemplateEngine.resolveTemplate(httpController.getRequestPath());
                HttpResponse.response200(dos, body, HttpRequestUtils.parseContentType(httpController.getRequestUrl()));
                break;
        }
    }
}
