package testController;

import annotation.RequestMapping;
import http.HttpResponse;
import http.HttpStatus;

import java.util.Map;

public class TestController {

    @RequestMapping(value = "/test", method = "GET")
    public HttpResponse getMethod(Map<String, String> query, String body) {
        byte[] content = "GET method called".getBytes();
        return new HttpResponse(content, HttpStatus.OK);
    }

    @RequestMapping(value = "/test", method = "POST")
    public HttpResponse postMethod(Map<String, String> query, String body) {
        byte[] content = "POST method called".getBytes();
        return new HttpResponse(content, HttpStatus.Found);
    }

    @RequestMapping(value = "/test/query", method = "GET")
    public HttpResponse getQuery(Map<String, String> query, String body) {
        byte[] content = query.toString().getBytes();
        return new HttpResponse(content, HttpStatus.OK);
    }

    @RequestMapping(value = "/test/body", method = "POST")
    public HttpResponse getBody(Map<String, String> query, String body) {
        byte[] content = body.getBytes();
        return new HttpResponse(content, HttpStatus.OK);
    }
}
