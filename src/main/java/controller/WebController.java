package controller;

import controller.request.Request;
import controller.response.Response;

/**
 * Created by melodist
 * Date: 2022-01-25 025
 * Time: 오후 6:26
 */
public interface WebController {
    Response process(Request request);
}
