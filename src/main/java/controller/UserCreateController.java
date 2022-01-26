package controller;

import dto.UserCreateRequestDto;
import http.HttpRequest;
import http.HttpResponse;

import java.util.Map;

public class UserCreateController implements Controller {


    @Override
    public void makeResponse(HttpRequest request, HttpResponse response) {

//        // GET 으로 회원가입 구현했을 때
//        Map<String, String> parsedQueryString = request.getParsedQueryString();
//        service.signUp(new UserCreateRequestDto(parsedQueryString.get("userId"), parsedQueryString.get("password"),
//                parsedQueryString.get("name"), parsedQueryString.get("email")));
//        response.makeMainPageRedirectResponse();

        // POST 로 회원가입 구현했을 때
        Map<String, String> parsedBody = request.getParsedBodyString();
        service.signUp(new UserCreateRequestDto(parsedBody.get("userId"), parsedBody.get("password"),
                parsedBody.get("name"), parsedBody.get("email")));
        response.makeMainPageRedirectResponse();
    }
}
