package application.controller;

import application.constants.CookieKeys;
import http.Cookies;
import http.request.HttpRequest;
import webapplication.data.AttributeTypes;
import webapplication.data.Model;
import webapplication.annotations.RequestMapping;
import application.db.DataBase;
import application.model.User;
import webapplication.annotations.data.RequestMethod;
import webapplication.dto.ModelAndView;

import java.util.List;

public class UserController {

    @RequestMapping(path = "/user/create", method = RequestMethod.POST)
    public static ModelAndView create(HttpRequest request) {
        String userId = (String) request.getParameter("userId");
        String password = (String) request.getParameter("password");
        String userName = (String) request.getParameter("name");
        String email = (String) request.getParameter("email");
        User user = new User(userId, password, userName, email);
        DataBase.addUser(user);
        return new ModelAndView("redirect:/index.html");
    }

    @RequestMapping(path = "/user/login", method = RequestMethod.POST)
    public static ModelAndView login(HttpRequest request) {
        String userId = (String) request.getParameter("userId");
        String password = (String) request.getParameter("password");

        User user = DataBase.findUserById(userId).orElse(null);
        if(user == null || !user.getPassword().equals(password)) {
            return new ModelAndView("redirect:/user/login_failed.html");
        }

        ModelAndView response = new ModelAndView("redirect:/index.html");
        Cookies cookies = new Cookies();
        cookies.setAttribute(CookieKeys.LOGINED, true);
        cookies.setAttribute(CookieKeys.AUTH_PATH, "/");
        response.addAttribute(AttributeTypes.COOKIES.getCode(), cookies);
        return response;
    }

    @RequestMapping(path = "/user/list", method = RequestMethod.GET)
    public static ModelAndView list(HttpRequest request) {
        Cookies cookies = request.getCookies();
        String isLogin = cookies.getAttribute(CookieKeys.LOGINED);
        if (!isLogin.equals("true")) {
            return new ModelAndView("redirect:/user/login.html");
        }

        List<User> users = DataBase.findAll();

        ModelAndView response = new ModelAndView("/user/list.html");
        response.addAttribute("users", users);
        return response;
    }

}
