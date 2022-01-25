package controller;

import util.ModelAndView;
import webserver.HttpRequest;
import webserver.config.WebConst;

import java.io.DataOutputStream;

public class MainController extends Controller{

    public MainController() {
        baseUrl = "";

        //모든 GET 처리
        runner.put("GET", (req, res) -> {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName(WebConst.URL_PREFIX + req.getRequestUri());
            return modelAndView;
        });
    }
    @Override
    public ModelAndView execute(HttpRequest httpRequest, DataOutputStream dos) {
        ControllerMethod controllerMethod = runner.get(httpRequest.getMethod() + httpRequest.getRequestUri());
        if(controllerMethod == null) {
            controllerMethod = runner.get("GET");
        }

        return controllerMethod.run(httpRequest, dos);
    }
}
