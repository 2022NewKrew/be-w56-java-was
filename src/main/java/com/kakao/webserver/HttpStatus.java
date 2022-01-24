package com.kakao.webserver;

import lombok.Getter;

@Getter
public enum HttpStatus {
    OK(200, "OK"),
    FOUND(302, "FOUND"),
    NOT_FOUND(404, "Not Found");

    private final int code;
    private final String text;

    HttpStatus(int code, String text) {
        this.code = code;
        this.text = text;
    }
}
