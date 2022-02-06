package controller;

import request.HttpRequest;
import response.HttpResponse.HttpResponseBuilder;
import response.HttpStatusCode;
import response.ResHeader;
import response.ResponseHeader;
import response.ResponseHeader.ResponseHeaderBuilder;
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
                ResponseHeader responseHeader = new ResponseHeaderBuilder()
                        .set(ResHeader.CONTENT_LENGTH, "" + body.length)
                        .build();

                return new HttpResponseBuilder(HttpStatusCode.OK)
                        .setHeader(responseHeader)
                        .setBody(body)
                        .build();
            }
        );
    }
}
