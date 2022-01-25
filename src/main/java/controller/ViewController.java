package controller;

import controller.WebController;
import controller.request.Request;

/**
 * Created by melodist
 * Date: 2022-01-25 025
 * Time: 오후 7:01
 */
public class ViewController implements WebController {
    @Override
    public String process(Request request) {
        return request.getPath();
    }
}
