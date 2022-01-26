package controller;

import collections.RequestHeaders;
import collections.RequestStartLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface Controller {

    static final Logger log = LoggerFactory.getLogger(Controller.class);

    void doResponse(String methodName, DataOutputStream dos, RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    default void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
