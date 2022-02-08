package com.my.was.container.handleradapters;

import com.my.was.controller.ResoureController;
import com.my.was.http.request.HttpRequest;
import com.my.was.http.response.HttpResponse;

public class ResourceHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupported(Object handler) {
        if (handler instanceof ResoureController) {
            return true;
        }

        return false;
    }

    @Override
    public void handle(Object handler, HttpRequest httpRequest, HttpResponse httpResponse) {
        ResoureController resoureController = (ResoureController) handler;
        resoureController.handle(httpRequest, httpResponse);
    }
}
