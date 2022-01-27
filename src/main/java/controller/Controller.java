package controller;

import mapper.ResponseSendDataModel;
import mapper.UrlMapper;
import util.UrlQueryUtils;
import webserver.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface Controller{
    Function<HttpRequest, ResponseSendDataModel> decideMethod(String method, String url);

    default ResponseSendDataModel run(HttpRequest httpRequest){
        if(decideMethod(httpRequest.getMethod(), httpRequest.getUrl()) == null){

            ResponseSendDataModel responseSendDataModel = new ResponseSendDataModel("/404_error.html");

            return responseSendDataModel;
        }
        return decideMethod(httpRequest.getMethod(), httpRequest.getUrl()).apply(httpRequest);
    }
}
