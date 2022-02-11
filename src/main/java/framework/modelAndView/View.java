package framework.modelAndView;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

abstract public class View {

    public abstract void render(ModelAndView mv, HttpRequest req, HttpResponse res) throws IOException;
}
