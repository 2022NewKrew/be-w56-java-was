package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class Response {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private String httpVesion;
    private String statusCode;
    private String statusText;
    private Map<String, String> headers;
    private String body;
    private DataOutputStream dos;

    public Response(OutputStream out) {
        dos = new DataOutputStream(out);
    }

    public void send(String path, String requestContext) throws IOException {
        byte[] body = HttpRequestUtils.matchURL(path);
        HttpResponseUtils.response200Header(dos, requestContext, body.length);
        HttpResponseUtils.responseBody(dos, body);
    }

    public void redirect(String path) {
        HttpResponseUtils.response302Header(dos, path);
    }
}
