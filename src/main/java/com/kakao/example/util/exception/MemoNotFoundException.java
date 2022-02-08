package com.kakao.example.util.exception;

import framework.util.exception.InternalServerErrorException;

public class MemoNotFoundException  extends InternalServerErrorException {
    public MemoNotFoundException(String msg) {
        super(msg);
    }

    public MemoNotFoundException() {
        super("해당 메모를 찾을 수 없습니다.");
    }
}
