package Controller;

import Service.HttpService;
import enums.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpResponseSender;

import java.io.OutputStream;

public class HttpController {

    private HttpRequest httpRequest;
    private HttpService httpService = new HttpService();
    private HttpResponseSender httpResponseSender;

    public HttpController(HttpRequest httpRequest, HttpResponse httpResponse, OutputStream out) {
        this.httpRequest = httpRequest;
        this.httpResponseSender = new HttpResponseSender(httpResponse, out);
    }

    public void run() {
        if (httpRequest.getMethod().equals(HttpMethod.POST) && httpRequest.getUrl().equals("/user/create")) {
            httpService.signup(httpRequest.getBody());
            httpResponseSender.sendResponse(httpRequest.getMethod(), "/index.html");
        }

        else if (httpRequest.getMethod().equals(HttpMethod.GET)) {
            httpResponseSender.sendResponse(httpRequest.getMethod(), "");
        }
    }

}
