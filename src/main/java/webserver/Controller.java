package webserver;

import webserver.annotation.GetMapping;

public class Controller {

    @GetMapping("/")
    public String getIndexPage() {

        return "/index.html";
    }
}
