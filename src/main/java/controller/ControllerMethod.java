package controller;

import model.Response;

import java.util.Map;

public interface ControllerMethod {
    Response run(Map<String, String> requestMap);
}
