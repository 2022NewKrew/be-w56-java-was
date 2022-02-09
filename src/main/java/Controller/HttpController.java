package Controller;

import Service.HttpService;
import enums.HttpMethod;
import http.HttpRequest;

public class HttpController {

    private final HttpRequest httpRequest;
    private final HttpService httpService = new HttpService();

    public HttpController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public String runServiceAndReturnPath() {
        // PostMapping("/user/create") 와 동일
        if (httpRequest.getMethod().equals(HttpMethod.POST) && httpRequest.getUrl().equals("/user/create")) {
            httpService.signup(httpRequest.getBody());
            return "redirect:/index.html";
        }
        // PostMapping("/user/login") 와 동일
        else if (httpRequest.getMethod().equals(HttpMethod.POST) && httpRequest.getUrl().equals("/user/login")) {
            boolean validLogin = httpService.validLogin(httpRequest.getBody().get("userId"), httpRequest.getBody().get("password"));
            if (validLogin) {
                return "redirect:/index.html";
            }
            else {
                return "redirect:/user/login_failed.html";
            }
        }
        // GetMapping("/user/list") 와 동일
        else if (httpRequest.getMethod().equals(HttpMethod.GET) && httpRequest.getUrl().equals("/user/list")) {
            return "/user/list.html";
        }
        // 이외 주소에 대한 GetMapping
        else if (httpRequest.getMethod().equals(HttpMethod.GET)) {
            return httpRequest.getUrl();
        }
        return "/index.html";
    }

}
