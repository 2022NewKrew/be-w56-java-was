package controller;

import mapper.ResponseSendDataModel;
import webserver.request.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RootController implements Controller{
    private final Map<String, Function<HttpRequest, ResponseSendDataModel>> methodMapper;

    public RootController(){
        methodMapper = new HashMap<>();

        methodMapper.put("GET /", this::index);
        methodMapper.put("GET /index.html", this::index);
    }

    private ResponseSendDataModel index(HttpRequest httpRequest){
        ResponseSendDataModel result = new ResponseSendDataModel("/index.html", httpRequest);

        return result;
    }

    @Override
    public Function<HttpRequest, ResponseSendDataModel> decideMethod(String method, String url) {
        url = method + " " + url;

        return methodMapper.get(url);
    }
}
