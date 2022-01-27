package common.dto;

import lombok.Builder;
import lombok.Getter;
import webserver.dto.response.HttpStatus;

import java.util.Map;

@Builder
@Getter
public class ControllerResponse {
    private final HttpStatus httpStatus;
    private final Map<String, String> header;
    private final String redirectTo;
}
