package adaptor.in.web.exception;

import infrastructure.exception.CustomResponseException;
import infrastructure.model.HttpStatus;

public class UriNotFoundException extends CustomResponseException {

    public UriNotFoundException() {
        super(HttpStatus.BAD_REQUEST, "{ \"error\" : 잘못 된 요청입니다. }");
    }
}
