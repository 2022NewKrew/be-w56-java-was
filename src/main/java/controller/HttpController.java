package controller;

import dao.MemoDao;
import dao.UserDao;
import enums.HttpStatus;
import model.Memo;
import org.slf4j.Logger;
import service.RequestService;
import util.DatabaseUtils;
import util.HttpRequestUtils;
import service.ResponseService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class HttpController {

    private Map<String, String> requestMap;
    private Map<String, String> headerMap;
    private Logger log;
    private BufferedReader br;
    private RequestService requestService;
    private ResponseService responseService;
    private UserDao userDao;
    private MemoDao memoDao;

    public HttpController(Map<String, String> requestMap, Map<String, String> headerMap,
                          Logger log, BufferedReader br, OutputStream out) throws SQLException {
        this.requestMap = requestMap;
        this.headerMap = headerMap;
        this.log = log;
        this.br = br;
        responseService = new ResponseService(out);
        Connection connection = DatabaseUtils.connect();
        userDao = new UserDao(connection);
        memoDao = new MemoDao(connection);
        requestService = new RequestService(userDao, memoDao);
    }

    public void run() throws IOException, SQLException {
        String url = requestMap.get("httpUrl");
        String cookie = null;
        HttpStatus httpStatus = HttpStatus.OK;
        String httpMethod = requestMap.get("httpMethod");
        Map<String, String> params = HttpRequestUtils.parseParams(httpMethod, url, headerMap, br);

        if(url.equals("/user/create") && httpMethod.equals("POST")) {
            requestService.createUser(params, log);
            httpStatus = HttpStatus.FOUND;
            url = "/index.html";
        }

        if(url.equals("/user/login") && httpMethod.equals("POST")) {
            cookie = requestService.userLogin(params, log);
            httpStatus = HttpStatus.FOUND;
            url = "/index.html";
        }

        if(url.equals("/user/list.html") && httpMethod.equals("GET")) {
            url = "/index.html";
            httpStatus = HttpStatus.FOUND;
            userList(url, httpStatus, cookie);
            return;
        }

        if(url.equals("/index.html") && httpMethod.equals("GET")) {
            String path = "./webapp/index.html";
            String tag = "{{#memos}}";
            String memos = requestService.getMemoList();
            makeFile(path, tag, memos, httpStatus);
            return;
        }

        if(url.equals("/memo/create") && httpMethod.equals("POST")) {
            String date = params.get("date");
            String writer = params.get("writer");
            String context = params.get("context");
            Memo memo = new Memo(date, writer, context);
            memoDao.addMemo(memo);
            System.out.println("실행");
            httpStatus = HttpStatus.FOUND;
            url = "/index.html";
        }

        responseService.response(url, httpStatus, cookie);
    }

    private void userList(String url, HttpStatus httpStatus, String cookie) throws IOException, SQLException {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(headerMap.get("Cookie"));
        boolean logined = Boolean.parseBoolean(cookies.get("logined"));
        if(logined) {
            httpStatus = HttpStatus.OK;
            String pathString = "./webapp/user/list.html";
            String tag = "{{#users}}";
            String users = requestService.getUserList();
            makeFile(pathString, tag, users, httpStatus);
            return;
        }
        responseService.response(url, httpStatus, cookie);
    }

    private void makeFile(String pathString, String tag, String tagValue, HttpStatus httpStatus) throws IOException {
        Path path = new File(pathString).toPath();
        StringBuilder html = new StringBuilder(Files.readString(path));
        html.replace(html.indexOf(tag), html.indexOf(tag) + tag.length(), tagValue);
        byte[] body = html.toString().getBytes();
        responseService.response200WithBody(path.toFile(), body, httpStatus);
    }

}
