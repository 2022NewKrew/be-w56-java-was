package webserver.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class RequestHeaders {
    Map<String, String> headerMap;
}
