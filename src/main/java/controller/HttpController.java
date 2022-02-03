package controller;

import enums.HttpStatus;
import org.slf4j.Logger;
import service.HtmlService;
import service.RequestService;
import util.HttpRequestUtils;
import service.ResponseService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class HttpController {

    private Map<String, String> requestMap;
    private Map<String, String> headerMap;
    private Logger log;
    private BufferedReader br;
    private RequestService requestService = new RequestService();
    private ResponseService responseService;
    private HtmlService htmlService;

    public HttpController(Map<String, String> requestMap, Map<String, String> headerMap,
                          Logger log, BufferedReader br, OutputStream out) {
        this.requestMap = requestMap;
        this.headerMap = headerMap;
        this.log = log;
        this.br = br;
        responseService = new ResponseService(out);
        htmlService = new HtmlService();
    }

    public void run() throws IOException {
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

        responseService.response(url, httpStatus, cookie);
    }

    private void userList(String url, HttpStatus httpStatus, String cookie) throws IOException {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(headerMap.get("Cookie"));
        boolean logined = Boolean.parseBoolean(cookies.get("logined"));
        if(logined) {
            htmlService.makeUserList();
            url = "/users/list.html";
            httpStatus = HttpStatus.OK;
        }
        responseService.response(url, httpStatus, cookie);
    }

}
