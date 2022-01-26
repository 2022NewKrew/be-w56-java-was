package controller;

import exception.IllegalRequestException;
import webserver.http.HttpGetMapper;
import webserver.http.ResponseData;
import webserver.http.request.Request;

public class Controller {

    public static ResponseData proceed(Request request) {
        HttpGetMapper httpMapper = HttpGetMapper.valueOfUrl(request.getUrl())
                .orElseThrow(() -> new IllegalRequestException("잘못된 URL 요청입니다."));
        return httpMapper.service(request);
    }
}
