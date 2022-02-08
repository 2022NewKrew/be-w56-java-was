package domain;

import webserver.ResponseHandler;

import java.io.DataOutputStream;

public class HttpResponse {

    public static void response200(DataOutputStream dos, byte[] body, ContentType contentType) {
        ResponseHandler responseHandler = new ResponseHandler(dos);
        responseHandler.response200Header(body.length, contentType.getContentType());
        responseHandler.responseBody(body);
    }

    public static void response302(DataOutputStream dos, String location) {
        ResponseHandler responseHandler = new ResponseHandler(dos);
        responseHandler.response302Header(location);
    }

    public static void response302(DataOutputStream dos, String location, String cookie) {
        ResponseHandler responseHandler = new ResponseHandler(dos);
        responseHandler.response302Header(location, cookie);
    }
}
