package framework.handler;

import util.HttpRequest;
import util.HttpResponse;

import java.io.IOException;

public abstract class Handler {

    abstract public String handle(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
    abstract public boolean isSupport(HttpRequest httpRequest);

}
