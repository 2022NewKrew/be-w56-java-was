package webserver.http.response;

import http.StatusCode;
import webserver.http.ResponseEntity;

public class RedirectResolver implements ResponseBodyResolver {
    @Override
    public boolean supports(ResponseEntity<?> responseEntity) {
        ResponseBody<?> responseBody = responseEntity.getResponseBody();
        return responseBody.getClass().isAssignableFrom(Redirect.class);
    }

    @Override
    public byte[] resolve(ResponseEntity<?> responseEntity) {
        Redirect redirect = (Redirect) responseEntity.getResponseBody();
        String redirectUrl = redirect.getResponseValue();
        responseEntity.setStatusCode(StatusCode.FOUND);
        responseEntity.addHeaders("Location", redirectUrl);
        return null;
    }
}
