package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.DispatcherServlet;
import webserver.annotation.RequestMapping;
import webserver.annotation.RequestMethod;

import java.util.Map;

public class KinaController {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final KinaController INSTANCE = new KinaController();

    private KinaController() {
    }

    public static KinaController getInstance() {
        return INSTANCE;
    }

    @RequestMapping(value = {"/", "/index", "/index.html"}, method = RequestMethod.GET)
    public String index(Map<String, String> queryMap) {
        log.info("KinaController - index()");
        return "/index.html";
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public String createUser(Map<String, String> queryMap) {
        log.info("KinaController - createUser() " + queryMap.entrySet().toString());
        User user = new User(
                queryMap.get("userId"),
                queryMap.get("password"),
                queryMap.get("name"),
                queryMap.get("email")
        );
        DataBase.addUser(user);
        log.info("DataBase.findAll(): " + DataBase.findAll().toString());
        return "redirect:/index.html";
    }

}
