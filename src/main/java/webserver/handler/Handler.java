package webserver.handler;

import webserver.request.Request;
import webserver.response.Response;

public interface Handler {

    Response handle(Request request) throws Exception;
}
