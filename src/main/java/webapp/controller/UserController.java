package webapp.controller;

import lombok.extern.slf4j.Slf4j;
import webserver.handler.annotation.Controller;
import webserver.handler.annotation.Param;
import webserver.handler.annotation.RequestMapping;
import webserver.http.HttpMethod;
import webserver.view.ModelAndView;

@Slf4j
@Controller
public class UserController {

    @RequestMapping(value = "/user/create", method = HttpMethod.GET)
    public ModelAndView createUser(@Param("userId") String userId, @Param("password") String password,
                                   @Param("name") String name, @Param("email") String email) {
        log.debug("In createUser, userId: {}, password: {}, name: {}, email: {}", userId, password, name, email);
        // ModelAndView mv = new ModelAndView();
        // mv.addAttribute();
        // mv.setViewName();
        // return mv;
        return null;
    }

    @RequestMapping(value = "/", method = HttpMethod.GET)
    public ModelAndView createUser() {
        return new ModelAndView("/index.html");
    }
}
