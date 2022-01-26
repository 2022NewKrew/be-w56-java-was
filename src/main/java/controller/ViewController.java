package controller;

import controller.WebController;
import controller.request.Request;
import controller.response.Response;
import util.HttpStatus;

/**
 * Created by melodist
 * Date: 2022-01-25 025
 * Time: 오후 7:01
 */
public class ViewController implements WebController {
    @Override
    public Response process(Request request) {
        return new Response(HttpStatus.OK, request.getPath());
    }
}
