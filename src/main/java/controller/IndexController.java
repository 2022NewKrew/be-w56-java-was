package controller;

import http.request.HttpRequest;
import http.request.HttpRequestMethod;
import http.response.HttpResponse.HttpResponseBuilder;
import http.response.HttpStatusCode;
import http.response.HttpResponseHeaderKey;
import http.response.HttpResponseHeader;
import http.response.HttpResponseHeader.ResponseHeaderBuilder;
import util.IOUtils;
import webserver.UrlMapper;

public class IndexController {

    private IndexController() {}

    public static void register() {

        UrlMapper.put(
            "/",
            HttpRequestMethod.GET,
            (HttpRequest httpRequest) -> {
                byte[] body = IOUtils.readFile("./webapp/index.html");
                HttpResponseHeader httpResponseHeader = new ResponseHeaderBuilder()
                        .set(HttpResponseHeaderKey.CONTENT_LENGTH, "" + body.length)
                        .build();

                return new HttpResponseBuilder(HttpStatusCode.OK)
                        .setHeader(httpResponseHeader)
                        .setBody(body)
                        .build();
            }
        );
    }
}
