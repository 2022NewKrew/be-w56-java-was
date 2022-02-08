package controller;

import mapper.ResponseSendDataModel;
import webserver.request.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StaticController implements Controller{
    private final Map<String, Function<HttpRequest, ResponseSendDataModel>> methodMapper;

    public StaticController(){
        methodMapper = new HashMap<>();

        methodMapper.put("GET /", this::staticFile);

    }

    private ResponseSendDataModel staticFile(HttpRequest httpRequest){
        ResponseSendDataModel result = new ResponseSendDataModel(httpRequest.getUrl(), httpRequest);

        return result;
    }

    @Override
    public Function<HttpRequest, ResponseSendDataModel> decideMethod(String method, String url) {
        url = method + " /";

        return methodMapper.get(url);
    }
}
