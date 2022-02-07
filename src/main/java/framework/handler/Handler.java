package framework.handler;

import framework.modelAndView.ModelAndView;
import util.HttpRequest;
import util.HttpResponse;

import java.io.IOException;

public abstract class Handler {

    abstract public ModelAndView handle(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
    abstract public boolean isSupport(HttpRequest httpRequest);

}
