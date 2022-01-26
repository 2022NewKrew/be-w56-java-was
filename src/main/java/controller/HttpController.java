package controller;

import enums.HttpStatus;
import org.slf4j.Logger;
import service.RequestService;
import util.HttpRequestUtils;
import service.ResponseService;
import util.IOUtils;

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

    public HttpController(Map<String, String> requestMap, Map<String, String> headerMap,
                          Logger log, BufferedReader br, OutputStream out) {
        this.requestMap = requestMap;
        this.headerMap = headerMap;
        this.log = log;
        this.br = br;
        responseService = new ResponseService(out);
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

        responseService.response(url, httpStatus, cookie);
    }

}
