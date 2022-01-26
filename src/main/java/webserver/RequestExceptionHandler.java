package webserver;

import webserver.exception.WebServerException;
import webserver.http.HttpStatus;
import webserver.http.MyHttpResponse;

public class RequestExceptionHandler {

    public static MyHttpResponse handle(WebServerException e) {
        if (e.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
            return ResponseProvider.responseServerException(e);
        }
        return ResponseProvider.responseClientException(e);
    }

}
