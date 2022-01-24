package http;

import java.net.URI;

public interface HttpRequest {
    HttpHeaders getHeaders();
    HttpMethod getMethod();
    URI getRequestUri();
    String getBody();
}
