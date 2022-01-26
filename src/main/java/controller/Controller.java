package controller;

import util.request.Request;
import util.response.Response;

import java.io.IOException;

public interface Controller {

    Response view(Request request, String url) throws IOException;
}
