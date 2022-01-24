package http;

public interface HttpResponse {
    HttpHeaders getHeaders();
    StatusCode getStatusCode();
    byte[] getResponseBody();
}
