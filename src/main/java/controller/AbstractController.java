package controller;

import http.Request;

public abstract class AbstractController {
    public abstract String controllerExecute(Request request);
}
