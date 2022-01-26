package webserver.controller;

import webserver.Response;
import webserver.web.request.Request;

import java.io.IOException;
import java.io.OutputStream;

public interface Controller {

    boolean isSupply(Request request);

    Response handle(Request request, OutputStream out) throws IOException;
}
