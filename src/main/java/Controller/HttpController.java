package Controller;

import Service.HttpService;
import enums.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpResponseSender;

import java.io.OutputStream;

public class HttpController {

    private final HttpRequest httpRequest;
    private final HttpService httpService = new HttpService();
    private final HttpResponseSender httpResponseSender;

    public HttpController(HttpRequest httpRequest, HttpResponse httpResponse, OutputStream out) {
        this.httpRequest = httpRequest;
        this.httpResponseSender = new HttpResponseSender(httpResponse, out);
    }

    public void run() {
        // PostMapping("/user/create") 와 동일
        if (httpRequest.getMethod().equals(HttpMethod.POST) && httpRequest.getUrl().equals("/user/create")) {
            httpService.signup(httpRequest.getBody());
            httpResponseSender.sendResponse302("/index.html");
        }
        // PostMapping("/user/login") 와 동일
        else if (httpRequest.getMethod().equals(HttpMethod.POST) && httpRequest.getUrl().equals("/user/login")) {
            boolean validLogin = httpService.validLogin(httpRequest.getBody().get("userId"), httpRequest.getBody().get("password"));
            httpResponseSender.sendResponseLogin(validLogin);
        }
        // GetMapping
        else if (httpRequest.getMethod().equals(HttpMethod.GET)) {
            httpResponseSender.sendResponse200();
        }
    }

}
