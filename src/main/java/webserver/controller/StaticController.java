package webserver.controller;

import DTO.ModelAndView;
import DTO.RequestHeader;
import DTO.ResponseHeader;
import Service.UserService;
import db.DataBase;
import model.User;

import java.util.Map;

public class StaticController implements Controller{

    @Override
    public ModelAndView getResponse(RequestHeader requestHeader, ResponseHeader responseHeader){
        Map<String, String> requestParam = requestHeader.getBody();
        String requestUrl= requestHeader.getRequestUrl();
        log.info("Static Controller : {}", requestUrl);
        return new ModelAndView(requestUrl);
    }
}
