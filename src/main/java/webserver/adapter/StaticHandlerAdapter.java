package webserver.adapter;

import java.io.File;
import java.io.IOException;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.util.Constant;

public class StaticHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean isSupport(HttpRequest request) {
        System.out.println("StaticHandlerAdapter.isSupport");
        File file = new File(Constant.ROOT_PATH + request.getUrl());
        return file.isFile();
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        System.out.println("StaticHandlerAdapter.handle");
        response.send(request.getUrl(), request.getContentType());
    }
}
