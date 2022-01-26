package controller;

import exception.IllegalRequestException;
import http.HttpGetMapper;
import http.ResponseData;
import http.request.Request;

public class ControllerDeprecated {

    public static ResponseData proceed(Request request) {
        HttpGetMapper httpMapper = HttpGetMapper.valueOfUrl(request.getUrl())
                .orElseThrow(() -> new IllegalRequestException("잘못된 URL 요청입니다."));
        return httpMapper.service(request);
    }
}
