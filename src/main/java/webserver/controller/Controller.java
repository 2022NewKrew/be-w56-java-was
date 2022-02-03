package webserver.controller;

import webserver.web.Parameters;
import webserver.web.request.Request;

import java.lang.reflect.InvocationTargetException;

public interface Controller {

    Object handle(Request request, Parameters requiredData) throws InvocationTargetException, IllegalAccessException;
}
