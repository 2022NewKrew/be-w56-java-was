package com.my.was.container.handlermappings;

import com.my.was.http.request.HttpRequest;

public interface HandlerMapping {

    Object findHandler(HttpRequest httpRequest);
}
