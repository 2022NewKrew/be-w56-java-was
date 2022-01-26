package controller;

import db.DataBase;
import lombok.extern.slf4j.Slf4j;
import model.User;
import webserver.annotation.RequestMapping;
import webserver.annotation.RequestMethod;

import java.util.Map;

@Slf4j
public class KinaController {

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
        User user = User.builder()
                .userId(queryMap.get("userId"))
                .password(queryMap.get("password"))
                .name(queryMap.get("name"))
                .email(queryMap.get("email"))
                .build();
        DataBase.addUser(user);
        log.info("DataBase.findAll(): " + DataBase.findAll().toString());
        return "redirect:/index.html";
    }

}
