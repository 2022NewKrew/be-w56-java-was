package com.my.was.container.handlermappings;

import com.my.was.controller.ResoureController;
import com.my.was.http.request.HttpRequest;

public class ResourceHandlerMapping implements HandlerMapping{
    @Override
    public Object findHandler(HttpRequest httpRequest) {
        return new ResoureController();
    }
}
