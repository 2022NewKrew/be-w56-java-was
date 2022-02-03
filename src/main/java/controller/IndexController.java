package controller;

import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponse.HttpResponseBuilder;
import response.HttpStatusCode;
import util.IOUtils;
import webserver.UrlMapper;

public class IndexController {

    private static final UrlMapper URL_MAPPER = UrlMapper.getInstance();

    private IndexController() {}

    public static void register() {

        URL_MAPPER.put(
            "/",
            "GET",
            (HttpRequest httpRequest) -> {
                byte[] body = IOUtils.readFile("./webapp/index.html");
                return new HttpResponseBuilder(HttpStatusCode.OK)
                    .setHeader(HttpResponse.defaultHeader(body.length))
                    .setBody(body)
                    .build();
            }
        );
    }
}
