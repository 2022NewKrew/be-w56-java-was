package webserver.handler;

import webserver.HttpRequest;

public abstract class Handler extends Thread{

//    abstract public String handle(HttpRequest httpRequest);
    abstract public void handle(HttpRequest httpRequest);
    abstract public Handler getHandlerWithRequest(HttpRequest httpRequest);
    abstract public boolean isSupport(HttpRequest httpReques);
}
