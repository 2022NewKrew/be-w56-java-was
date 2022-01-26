package controller;

import was.domain.controller.Controller;
import was.meta.HttpStatus;

import java.nio.charset.StandardCharsets;

public class HomeController {
    public Controller home = (req, res) -> {
        res.setStatus(HttpStatus.OK);
        res.setBody("hello".getBytes(StandardCharsets.UTF_8));
    };
}
