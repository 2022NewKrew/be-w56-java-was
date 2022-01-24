package controller;

import webserver.HttpRequest;
import webserver.config.WebConst;

import java.io.DataOutputStream;

public class MainController implements Controller{
    @Override
    public boolean isSupport(String url) {
        //다 처리해주는 컨트롤러 여기서 없으면 IOException;
        return true;
    }

    @Override
    public String execute(HttpRequest httpRequest, DataOutputStream dos) {
        // method 와 url 을 가져와서 적절한 메소드 호출
        if(httpRequest.getMethod().equals("GET")) {
            return WebConst.URL_PREFIX + httpRequest.getRequestUri();
        }
        return "";
    }
}
