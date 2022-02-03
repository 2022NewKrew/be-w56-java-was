package controller;

import collections.RequestBody;
import collections.RequestHeaders;
import collections.RequestStartLine;
import collections.ResponseHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public interface Controller {

    Logger log = LoggerFactory.getLogger(Controller.class);

    void doResponse(String methodName, DataOutputStream dos, RequestStartLine requestStartLine, RequestHeaders requestHeaders, RequestBody requestBody) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    default void responseStatusLine(DataOutputStream dos, String statusLine) {
        try {
            dos.writeBytes(statusLine + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void responseHeader(DataOutputStream dos, ResponseHeaders responseHeaders) {
        try {
            Set<String> headerKeys = responseHeaders.getHeaderKeys();
            for (String headerKey : headerKeys) {
                dos.writeBytes(headerKey + ": " + responseHeaders.getHeader(headerKey) + "\r\n");
            }

            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    default void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
