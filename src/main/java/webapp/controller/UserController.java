package webapp.controller;

import lombok.extern.slf4j.Slf4j;
import webserver.handler.annotation.Controller;
import webserver.handler.annotation.Param;
import webserver.handler.annotation.RequestMapping;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

@Slf4j
@Controller
public class UserController {

    @RequestMapping(value = "/user/create", method = HttpMethod.GET)
    public String createUser(@Param("ts") String ts, HttpRequest request, HttpResponse response, @Param("hi") int hi) {
        log.info("In createUser");
        log.info(ts);
        log.info(String.valueOf(request));
        log.info(String.valueOf(response));
        return "/";
    }

    @RequestMapping(value = "/", method = HttpMethod.GET)
    public String createUser(HttpRequest request, HttpResponse response) {
        log.info("In index");
        log.info(String.valueOf(request));
        log.info(String.valueOf(response));
        return "/";
    }
}
