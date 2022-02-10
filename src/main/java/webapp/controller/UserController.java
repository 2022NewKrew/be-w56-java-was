package webapp.controller;

import lombok.extern.slf4j.Slf4j;
import webserver.handler.annotation.Controller;
import webserver.handler.annotation.Param;
import webserver.handler.annotation.RequestMapping;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.view.ModelAndView;

@Slf4j
@Controller
public class UserController {

    @RequestMapping(value = "/user/create", method = HttpMethod.GET)
    public ModelAndView createUser(@Param("userId") String userId, @Param("password") String password,
                                   @Param("name") String name, @Param("email") String email) {
        log.debug("In createUser, userId: {}, password: {}, name: {}, email: {}", userId, password, name, email);
        // Call method to actually register user here
        return new ModelAndView("/index.html");
    }

    @RequestMapping(value = "/login")
    public ModelAndView login(HttpResponse httpResponse) {
        // Implement authentication here
        httpResponse.setCookie("sessionId", "someRandomSessionValue");
        return new ModelAndView("/index.html");
    }

    @RequestMapping(value = "/user/list")
    public ModelAndView listUsers(HttpRequest httpRequest) {
        // Example login test
        String sessionId = httpRequest.getCookies().get("sessionId");
        // Retrieve user information from session database by this sessionId;
        log.debug("Session Id: {}", sessionId);
        ModelAndView mv = new ModelAndView("/user/list.html");
        mv.addAttribute("userId", "testUserId");
        mv.addAttribute("name", "testName");
        mv.addAttribute("email", "testEmail");
        return mv;
    }

    @RequestMapping
    public ModelAndView createUser() {
        return new ModelAndView("/index.html");
    }
}
