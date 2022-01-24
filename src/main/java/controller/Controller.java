package controller;

import webserver.HttpRequest;

import java.io.DataOutputStream;

public interface Controller {
    public boolean isSupport(String url);
    public String execute(HttpRequest httpRequest, DataOutputStream dos);
}
