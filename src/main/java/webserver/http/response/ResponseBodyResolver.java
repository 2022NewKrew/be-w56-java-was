package webserver.http.response;

import webserver.http.ResponseEntity;

public interface ResponseBodyResolver {
    boolean supports(ResponseEntity<?> responseEntity);
    byte[] resolve(ResponseEntity<?> responseEntity);
}
