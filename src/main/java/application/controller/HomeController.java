package application.controller;

import was.controller.Controller;
import was.meta.HttpHeader;
import was.meta.HttpStatus;
import was.meta.UrlPath;

public class HomeController {

    public Controller home = (request, response) -> {
        response.setStatus(HttpStatus.FOUND);
        response.addHeader(HttpHeader.LOCATION, UrlPath.INDEX.getPath());
    };

}
