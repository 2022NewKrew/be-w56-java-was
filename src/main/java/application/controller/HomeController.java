package application.controller;

import di.annotation.Bean;
import was.http.annotation.Controller;
import was.http.annotation.GetMapping;
import was.http.domain.service.view.ModelAndView;
import was.http.domain.service.view.ViewType;

@Controller
@Bean
public class HomeController {

    @GetMapping(path = "/")
    public ModelAndView home() {
        return new ModelAndView(ViewType.FORWARD, "/users");
    }
}
