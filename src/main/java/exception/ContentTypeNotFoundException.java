package exception;

import http.HttpStatus;

public class ContentTypeNotFoundException extends CustomException {

    private static final String MESSAGE = "일치하는 content-type 을 찾을 수 없습니다.";

    public ContentTypeNotFoundException() {
        super(MESSAGE, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
