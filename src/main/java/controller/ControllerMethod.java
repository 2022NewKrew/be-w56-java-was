package controller;

import util.ModelAndView;
import webserver.HttpRequest;

import java.io.DataOutputStream;

@FunctionalInterface
public interface ControllerMethod {
    ModelAndView run(HttpRequest httpRequest, DataOutputStream res);
}
