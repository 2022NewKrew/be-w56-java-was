package controller;

import model.Request;
import model.Response;

public interface Controller {

    String USER = "/user";

    Response routing(String urlPath, Request request);
}
