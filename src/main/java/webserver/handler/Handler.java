package webserver.handler;

import webserver.request.Request;
import webserver.response.Response;

public interface Handler {

    void handle(Request request, Response response) throws Exception;
}
