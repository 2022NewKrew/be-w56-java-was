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

    public static Response createResponse(Request request, HttpStatusCode httpStatusCode, ContentType contentType, ResponseBody responseBody) {
        StatusLine statusLine = StatusLine.of(request.getRequestLine().getHttpVersion(), httpStatusCode);
        ResponseHeader responseHeader = ResponseHeader.of(contentType.getType(), responseBody != null ? responseBody.getBody().length : 0);
        return Response.of(statusLine, responseHeader, responseBody);
    }

    public static Response createResponse(Request request, HttpStatusCode httpStatusCode) {
        return createResponse(request, httpStatusCode, ContentType.DEFAULT, null);
    }

    public static Response createResponse(Request request, HttpStatusCode httpStatusCode, ContentType contentType) {
        return createResponse(request, httpStatusCode, contentType, null);
    }

    public static ResponseBody getRedirection(String path) throws IOException {
        return ResponseBody.from(Files.readAllBytes(new File(path).toPath()));
    }

    public static Response createStaticResourceResponse(Request request, HttpStatusCode httpStatusCode) throws IOException {
        return createResponse(request, httpStatusCode, ContentType.TEXT_HTML, ResponseBody.from(Files.readAllBytes(new File("./webapp" + request.getRequestLine().getRequestUri().getPath()).toPath())));
    }

    public static Response createErrorResponse() throws IOException {
        log.info("Create Error Response");
        ResponseBody responseBody = getRedirection("./webapp/error.html");
        return Response.of(
                StatusLine.of("HTTP/1.1", HttpStatusCode.BAD_REQUEST),
                ResponseHeader.of("text/html; charset=utf-8", responseBody.getBody().length),
                responseBody
        );
    }
    public static void setDataOutputStream(DataOutputStream dos, Response response) throws IOException {
        log.info("Set DataOutputStream");
        responseHeader(dos, response);
        if(response.getResponseBody() != null) {
            responseBody(dos, response.getResponseBody().getBody());
        }
        dos.flush();
    }
    public static void responseHeader(DataOutputStream dos, Response response) {
        log.info("Response Header");
        try {
            StatusLine statusLine = response.getStatusLine();
            ResponseHeader responseHeader = response.getResponseHeader();
            System.out.println(statusLine.getHttpVersion() + " " + statusLine.getHttpStatusCode() + " " + statusLine.getHttpStatusCode().getCode() + "\r\n");
            dos.writeBytes(statusLine.getHttpVersion() + " " + statusLine.getHttpStatusCode() + " " + statusLine.getHttpStatusCode().getCode() + "\r\n");
            System.out.println(CONTENT_TYPE + ": "+ responseHeader.getContentType() + "\r\n");
            dos.writeBytes(CONTENT_TYPE + ": "+ responseHeader.getContentType() + "\r\n");
            System.out.println(CONTENT_LENGTH + ": " + responseHeader.getContentLength() + "\r\n");
            dos.writeBytes(CONTENT_LENGTH + ": " + responseHeader.getContentLength() + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void responseBody(DataOutputStream dos, byte[] body) {
        log.info("Response Body");
        try {
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
