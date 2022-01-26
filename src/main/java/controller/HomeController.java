package controller;

import http.GetMapping;
import http.HttpRequest;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpRequest httpRequest) {
        return "/index.html";
    }
}
