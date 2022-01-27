package adaptor.in.web.exception;

import infrastructure.exception.CustomResponseException;
import infrastructure.model.HttpStatus;

public class FileNotFoundException extends CustomResponseException {

    public FileNotFoundException() {
        super(HttpStatus.NOT_FOUND, "{ \"error\" : 요청 받은 리소스를 찾을 수 없습니다. }");
    }
}
