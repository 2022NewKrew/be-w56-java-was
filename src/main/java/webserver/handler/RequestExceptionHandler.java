package webserver.handler;

import webserver.provider.ResponseProvider;
import webserver.exception.WebServerException;
import webserver.http.HttpStatus;
import webserver.http.HttpResponse;

public class RequestExceptionHandler {

    public static HttpResponse handle(Exception e) {
        if (e instanceof WebServerException) {
            return handleWebServerException(e);
        }
        return ResponseProvider.responseServerException(e);
    }

    private static HttpResponse handleWebServerException(Exception exception) {
        WebServerException e = (WebServerException) exception;

        if (e.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
            return ResponseProvider.responseServerException(e);
        }
        return ResponseProvider.responseClientException(e);
    }
}
