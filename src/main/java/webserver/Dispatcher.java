package webserver;

import webserver.context.Request;

import java.io.IOException;
import java.io.OutputStream;

public interface Dispatcher {

    void dispatch(Request request, OutputStream out) throws IOException;

}
