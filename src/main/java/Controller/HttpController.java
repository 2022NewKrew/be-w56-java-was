package Controller;

import Service.HttpService;
import db.DataBase;
import enums.HttpMethod;
import http.HttpRequest;
import http.ResponseBuildInfo;
import util.DynamicHtmlBuilder;

public class HttpController {

    private final HttpRequest httpRequest;
    private final HttpService httpService = new HttpService();

    public HttpController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public ResponseBuildInfo runServiceAndReturnInfo() {

        // PostMapping("/user/create") 와 동일
        if (httpRequest.getMethod().equals(HttpMethod.POST) && httpRequest.getUrl().equals("/user/create")) {
            httpService.signup(httpRequest.getBody());
            return new ResponseBuildInfo.InfoBuilder().setPath("/index.html").setRedirect(true).build();
        }
        // PostMapping("/user/login") 와 동일
        else if (httpRequest.getMethod().equals(HttpMethod.POST) && httpRequest.getUrl().equals("/user/login")) {
            boolean validLogin = httpService.validLogin(httpRequest.getBody().get("userId"), httpRequest.getBody().get("password"));
            if (validLogin) {
                return new ResponseBuildInfo.InfoBuilder()
                        .setPath("/index.html")
                        .setRedirect(true)
                        .addCookie("logined", "true")
                        .addCookie("Path", "/")
                        .build();
            }
            else {
                return new ResponseBuildInfo.InfoBuilder()
                        .setPath("/user/login_failed.html")
                        .setRedirect(true)
                        .addCookie("logined", "false")
                        .addCookie("Path", "/")
                        .build();
            }
        }
        // GetMapping("/user/list") 와 동일
        else if (httpRequest.getMethod().equals(HttpMethod.GET) && httpRequest.getUrl().equals("/user/list")) {
            if (httpRequest.getCookie().containsKey("logined") && httpRequest.getCookie().get("logined").equals("true")) {
                DynamicHtmlBuilder.build("user/list.html", DataBase.findAll());
                return new ResponseBuildInfo.InfoBuilder().setPath("/user/dynamicList.html").build();
            }
            return new ResponseBuildInfo.InfoBuilder().setPath("/user/login.html").build();
        }
        // 이외 주소에 대한 GetMapping
        else if (httpRequest.getMethod().equals(HttpMethod.GET)) {
            return new ResponseBuildInfo.InfoBuilder().setPath(httpRequest.getUrl()).build();
        }
        return new ResponseBuildInfo.InfoBuilder().setPath("index.html").build();
    }

}
