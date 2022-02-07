package framework.modelAndView;

import util.HttpRequest;
import util.HttpResponse;

import java.io.IOException;

abstract public class View {

    public abstract void render(ModelAndView mv, HttpRequest req, HttpResponse res) throws IOException;
}
