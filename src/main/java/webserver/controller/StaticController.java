package webserver.controller;

import annotation.GetMapping;

@annotation.Controller
public class StaticController extends BaseController {

    private static final StaticController staticController = new StaticController();

    private StaticController() {
    }

    public static StaticController getInstance() {
        return staticController;
    }

    @GetMapping(url = "/")
    public String staticFile(String filePath) {
        return filePath;
    }
}
