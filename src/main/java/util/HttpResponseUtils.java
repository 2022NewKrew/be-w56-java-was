package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.Request;
import webserver.response.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HttpResponseUtils {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final Logger log = LoggerFactory.getLogger(HttpResponseUtils.class);

    public static Response createResponse(Request request, HttpStatusCode httpStatusCode, ResponseBody responseBody) {
        StatusLine statusLine = StatusLine.of(request.getRequestLine().getHttpVersion(), httpStatusCode);
        ResponseHeader responseHeader = ResponseHeader.of(request.getRequestHeader().getContentType(), responseBody.getBody().length);
        return Response.of(statusLine, responseHeader, responseBody);
    }

    public static Response createResponse(Request request, HttpStatusCode httpStatusCode) throws IOException {
        return createResponse(request, httpStatusCode, ResponseBody.from(Files.readAllBytes(new File("./webapp/index.html").toPath())));
    }

    public static Response createStaticResourceResponse(Request request, HttpStatusCode httpStatusCode) throws IOException {
        return createResponse(request, httpStatusCode, ResponseBody.from(Files.readAllBytes(new File("./webapp" + request.getRequestLine().getRequestUri().getPath()).toPath())));
    }

    public static Response createErrorResponse() throws IOException {
        ResponseBody responseBody = ResponseBody.from(Files.readAllBytes(new File("./webapp/error.html").toPath()));
        return Response.of(
                StatusLine.of("HTTP/1.1", HttpStatusCode.BAD_REQUEST),
                ResponseHeader.of("text/html; charset=utf-8", responseBody.getBody().length),
                responseBody
        );
    }
    public static void setDataOutputStream(DataOutputStream dos, Response response) {
        responseHeader(dos, response);
        responseBody(dos, response.getResponseBody().getBody());
    }
    public static void responseHeader(DataOutputStream dos, Response response) {
        try {
            StatusLine statusLine = response.getStatusLine();
            ResponseHeader responseHeader = response.getResponseHeader();
            dos.writeBytes(statusLine.getHttpVersion() + " " + statusLine.getHttpStatusCode() + " " + statusLine.getHttpStatusCode().getCode() + "\r\n");
            dos.writeBytes(CONTENT_TYPE + ": "+ responseHeader.getContentType() + "\r\n");
            dos.writeBytes(CONTENT_LENGTH + ": " + responseHeader.getContentLength() + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
