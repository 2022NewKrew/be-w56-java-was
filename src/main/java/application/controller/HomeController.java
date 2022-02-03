package application.controller;

import di.annotation.Bean;
import was.domain.controller.annotation.Controller;
import was.domain.controller.annotation.GetMapping;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.meta.HttpStatus;

import java.nio.charset.StandardCharsets;

@Controller
@Bean
public class HomeController {

    @GetMapping(path = "/")
    public void home(HttpRequest req, HttpResponse res) {
        res.setStatus(HttpStatus.OK);
        res.setBody("hello".getBytes(StandardCharsets.UTF_8));
    }

}
