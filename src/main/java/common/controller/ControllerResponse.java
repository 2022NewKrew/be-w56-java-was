package common.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webserver.requesthandler.httpresponse.HttpStatus;

import java.util.Map;

@AllArgsConstructor
@Getter
public class ControllerResponse {
    private final HttpStatus httpStatus;
    private final Map<String, String> header;
    private final String redirectTo;
}
