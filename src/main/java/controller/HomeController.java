package controller;

import http.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "/index.html";
    }
}
