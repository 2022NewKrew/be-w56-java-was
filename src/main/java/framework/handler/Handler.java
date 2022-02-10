package framework.handler;

import framework.modelAndView.ModelAndView;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

public abstract class Handler {

    abstract public ModelAndView handle(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
    abstract public boolean isSupport(HttpRequest httpRequest);

}
