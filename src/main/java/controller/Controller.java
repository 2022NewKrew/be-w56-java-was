package controller;

import annotation.GetMapping;

public class Controller {

    @GetMapping(path = "/")
    public String getIndex() {

        return "/index.html";
    }

}
