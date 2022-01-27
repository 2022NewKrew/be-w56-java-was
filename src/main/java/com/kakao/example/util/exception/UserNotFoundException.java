package com.kakao.example.util.exception;

import framework.util.exception.InternalServerErrorException;

public class UserNotFoundException extends InternalServerErrorException {
    public UserNotFoundException(String msg) {
        super(msg);
    }

    public UserNotFoundException() {
        super("해당 유저를 찾을 수 없습니다.");
    }
}
