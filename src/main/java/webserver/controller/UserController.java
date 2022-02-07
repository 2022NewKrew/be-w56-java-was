package webserver.controller;

import annotation.RequestMapping;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.*;
import webserver.service.UserService;
import webserver.view.ModelAndView;

public class UserController {
    private static final UserService userService = new UserService();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final UserController instance = new UserController();

    private UserController() {
    }

    public static UserController getInstance() {
        return instance;
    }

    @RequestMapping(method = HttpMethod.GET, url = "/")
    public ModelAndView home(HttpRequest request, HttpResponse response){
        return new ModelAndView("/index.html");
    }

    @RequestMapping(method = HttpMethod.POST, url = "/user/create")
    public ModelAndView register(HttpRequest request, HttpResponse response) {
        User user = new User(
                request.getBody("userId"),
                request.getBody("password"),
                request.getBody("name"),
                request.getBody("email")
        );

        userService.join(user);
        log.info("Create User - {}", user);

        return new ModelAndView("redirect:/index.html");
    }

    @RequestMapping(method = HttpMethod.GET, url = "/user/login.html")
    public ModelAndView loginForm(HttpRequest request, HttpResponse response){
        String logined = request.getCookie(HttpConst.COOKIE_LOGINED);

        if((logined != null) && logined.equals("true")){
            return new ModelAndView("redirect:/index.html");
        }

        return new ModelAndView("/user/login.html");
    }


    @RequestMapping(method = HttpMethod.POST, url = "/login")
    public ModelAndView login(HttpRequest request, HttpResponse response) {
        String userId = request.getBody("userId");
        String password = request.getBody("password");

        User user = userService.findUser(userId);


        if (user == null || !user.getPassword().equals(password)) {
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setCookie(HttpConst.COOKIE_LOGINED, "false");
            return new ModelAndView(HttpConst.LOGIN_FAIL_PAGE);
        }

        response.setCookie(HttpConst.COOKIE_LOGINED, "true; Path=/");
        return new ModelAndView("redirect:/index.html");
    }

    @RequestMapping(method = HttpMethod.GET, url = "/user/list")
    public ModelAndView userList(HttpRequest request, HttpResponse response){
        String logined = request.getCookie(HttpConst.COOKIE_LOGINED);

        if(logined == null || logined.equals("false")){
            response.setStatus(HttpStatus.UNAUTHORIZED);
            return new ModelAndView(HttpConst.LOGIN_PAGE);
        }

        ModelAndView mv = new ModelAndView("/user/list.html");
        mv.addAttribute("userList", userService.findAllUser());

        return mv;
    }
}
