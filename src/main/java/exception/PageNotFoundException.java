package exception;

import http.HttpStatus;

public class PageNotFoundException extends CustomException {

    private static final String MESSAGE = "페이지를 찾을 수 없습니다.";

    public PageNotFoundException() {
        super(MESSAGE, HttpStatus.PAGE_NOT_FOUND);
    }
}
