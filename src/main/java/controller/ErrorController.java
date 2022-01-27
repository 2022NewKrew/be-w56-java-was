package controller;

import mapper.AssignedModelKey;
import mapper.ResponseSendDataModel;
import webserver.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ErrorController implements Controller{
    private final Map<String, Function<HttpRequest, ResponseSendDataModel>> methodMapper;

    public ErrorController(){
        methodMapper = new HashMap<>();

        methodMapper.put("GET /error", this::notFoundError);
        methodMapper.put("POST /error", this::notFoundError);
    }

    private ResponseSendDataModel notFoundError(HttpRequest httpRequest){
        ResponseSendDataModel result = new ResponseSendDataModel("/404_error.html");

        return result;
    }

    @Override
    public Function<HttpRequest, ResponseSendDataModel> decideMethod(String method, String url) {
        url = method + " /error";

        return methodMapper.get(url);
    }
}
