package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.provider.ResponseProvider;
import webserver.exception.WebServerException;
import webserver.http.HttpStatus;
import webserver.http.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RequestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RequestExceptionHandler.class);

    public static HttpResponse handle(Socket connection, Exception e) {
        DataOutputStream dos;
        try {
            dos = new DataOutputStream(connection.getOutputStream());
        } catch (IOException ex) {
            log.error("에러: 예외 응답을 처리하는 과정에서 I/O 예외가 발생했습니다: {}", ex.getMessage());
            return null;
        }

        if (e instanceof WebServerException) {
            return handleWebServerException(dos, e);
        }
        return ResponseProvider.responseServerException(dos, e);
    }

    private static HttpResponse handleWebServerException(DataOutputStream dos, Exception exception) {
        WebServerException e = (WebServerException) exception;

        if (e.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
            return ResponseProvider.responseServerException(dos, e);
        }
        return ResponseProvider.responseClientException(dos, e);
    }
}
