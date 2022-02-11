package controller;

import annotation.RequestMapping;
import service.MemoService;
import webserver.ResponseGenerator;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.PathInfo;

public class RootController {
    private final MemoService memoService = new MemoService();

    @RequestMapping(value = PathInfo.PATH_INDEX, requestMethod = "GET")
    public HttpResponse getIndexPage() {
        try {
            return ResponseGenerator.generateIndexResponse(memoService.findAll());
        } catch (Exception e) {
            return ResponseGenerator.generateResponse500();
        }
    }
}
