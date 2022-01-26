package webserver.controller;

import webserver.Request;

import java.io.IOException;

public interface Controller {
    String control(Request request) throws IOException;
}
