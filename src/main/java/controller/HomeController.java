package controller;

import mvcframework.RequestMethod;
import mvcframework.annotation.Controller;
import mvcframework.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;
import webserver.HttpResponse;

@Controller
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void index(HttpRequest request, HttpResponse response) {
        System.out.println("call index");
        response.sendRedirect("/index.html");
    }
}
