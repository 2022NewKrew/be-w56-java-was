package common.dto;

import lombok.Builder;
import lombok.Getter;
import webserver.dto.request.HttpMethod;

import java.util.Map;

@Builder
@Getter
public class ControllerRequest {
    private final HttpMethod httpMethod;
    private final String url;
    private final Map<String, String> header;
    private final Map<String, String> body;
}
