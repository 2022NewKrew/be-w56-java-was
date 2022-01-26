package webserver;

import webserver.exception.BadRequestException;
import webserver.exception.InvalidMethodException;
import webserver.exception.ResourceNotFoundException;
import webserver.exception.WebServerException;
import webserver.http.MyHttpResponse;

public class RequestExceptionHandler {

    public static MyHttpResponse handle(WebServerException e) {
        if (e instanceof BadRequestException) {
            return ResponseProvider.response400BadRequest(e);
        }
        if (e instanceof ResourceNotFoundException) {
            return ResponseProvider.response404NotFound(e);
        }
        if (e instanceof InvalidMethodException) {
            return ResponseProvider.response405MethodNotAllowed(e);
        }
        return ResponseProvider.response500InternalServerError(e);
    }

}
