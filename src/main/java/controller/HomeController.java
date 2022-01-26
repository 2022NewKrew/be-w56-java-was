package controller;

import webserver.annotation.Controller;
import webserver.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "/index.html";
    }
}
