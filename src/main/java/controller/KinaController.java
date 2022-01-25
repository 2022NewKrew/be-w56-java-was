package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.DispatcherServlet;
import webserver.annotation.RequestMapping;
import webserver.annotation.RequestMethod;

public class KinaController {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final KinaController INSTANCE = new KinaController();

    private KinaController() {
    }

    public static KinaController getInstance() {
        return INSTANCE;
    }

    @RequestMapping(value = {"/", "/index", "/index.html"}, method = RequestMethod.GET)
    public String index() {
        log.info("KinaController - index()");
        return "/index.html";
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public String createUser() {
        log.info("KinaController - createUser()");
        return "/user/create";
    }

}
