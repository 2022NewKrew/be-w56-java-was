package error;

import lombok.extern.slf4j.Slf4j;
import webserver.web.response.HttpStatus;
import webserver.web.response.Response;

@Slf4j
public class ErrorHandler {

    public static Response handle(Exception e) {
        log.error("ErrorHandler : {}", e.getMessage());
        return new Response.ResponseBuilder().setStatus(HttpStatus.NOT_FOUND).build();
    }
}
