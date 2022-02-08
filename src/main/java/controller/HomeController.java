package controller;

import http.GetMapping;
import http.HttpResponse;

@Controller
public class HomeController {

    @GetMapping("/")
    public void home(HttpResponse httpResponse) {
        httpResponse.ok("/index.html");
    }
}
