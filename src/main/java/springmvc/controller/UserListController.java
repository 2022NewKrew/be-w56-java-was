package springmvc.controller;

import model.User;
import springmvc.ModelAndView;
import springmvc.db.DataBase;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListController extends Controller {

    @Override
    protected ModelAndView doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.getCookies().getOrDefault("logined", "false").equals("true")){
            List<User> users = List.copyOf(DataBase.findAll());
            Map<String, Object> model = new HashMap<>();
            model.put("users", users);
            return new ModelAndView("/user/list.html", HttpStatus.OK, model);
        }
        return new ModelAndView("/user/login.html", HttpStatus.FOUND);
    }


}
