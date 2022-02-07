package controller;

import http.HttpMethod;
import http.request.Request;
import model.ModelAndView;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.RequestMapping;

public class UserController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService = UserService.getInstance();

    private static final UserController userController = new UserController();

    private UserController(){}

    public static UserController getInstance(){
        return userController;
    }


    @RequestMapping(method = HttpMethod.GET, url = "/users/form")
    public ModelAndView createUserForm(Request request) {
        log.info("[UserController] : createUser");
        return new ModelAndView("/users/form");
    }

    @RequestMapping(method = HttpMethod.POST, url = "/users")
    public ModelAndView signupUser(Request request){
        log.info("[UserController] : signupUser");
        User user = userService.createUser(request.getRequestBody());
        return new ModelAndView("redirect:/", "user", user);
    }

    @RequestMapping(method = HttpMethod.GET, url = "/users/login")
    public ModelAndView createLoginForm(Request request){
        log.info("[UserController] : createLoginForm");
        return new ModelAndView("/users/login");
    }

    @RequestMapping(method = HttpMethod.POST, url = "/users/login")
    public ModelAndView loginUser(Request request){
        log.info("[UserController] : loginUser");
        boolean login = userService.login(request.getRequestBody());
        ModelAndView mv = new ModelAndView("redirect:/");
        mv.addObject("login", login);
        if(login){
            mv.addObject("user", userService.searchUserById(request.getRequestBody()));
        }
        return mv;
    }
}
