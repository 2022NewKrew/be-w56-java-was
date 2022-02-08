package com.kakao.example.util.exception;

import framework.util.exception.InternalServerErrorException;

public class NotLoggedInException extends InternalServerErrorException {
    public NotLoggedInException(String msg) {
        super(msg);
    }

    public NotLoggedInException() {
        super("로그인 상태가 아닙니다.");
    }
}