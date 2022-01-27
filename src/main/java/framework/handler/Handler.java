package framework.handler;

import util.HttpRequest;
import util.HttpResponse;

public abstract class Handler extends Thread{

    abstract public String handle(HttpRequest httpRequest, HttpResponse httpResponse);
    abstract public boolean isSupport(HttpRequest httpRequest);
}
