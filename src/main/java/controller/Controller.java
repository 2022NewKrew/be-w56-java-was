package controller;

import model.ModelAndView;
import http.request.Request;

public interface Controller {
    ModelAndView proceed(Request request);
}
