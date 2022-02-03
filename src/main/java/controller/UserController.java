package controller;

import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponse.HttpResponseBuilder;
import response.HttpStatusCode;
import util.IOUtils;
import webserver.UrlMapper;

public class UserController {

    private static final UrlMapper URL_MAPPER = UrlMapper.getInstance();

    private UserController() {}

    public static void register() {

        URL_MAPPER.put(
            "/user/form.html",
            "GET",
            (HttpRequest httpRequest) -> {
                byte[] body = IOUtils.readFile("./webapp/user/form.html");
                return new HttpResponseBuilder(HttpStatusCode.OK)
                    .setHeader(HttpResponse.defaultHeader(body.length))
                    .setBody(body)
                    .build();
            }
        );

//        UrlMapper.INSTANCE.put(
//            "user/form.html",
//            "POST",
//            (HttpRequest httpRequest, HttpResponse httpResponse) -> {
//                return new HttpResponse();
//            }
//        );
    }
}
