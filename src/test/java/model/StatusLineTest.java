package model;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StatusLineTest {

    @Test
    @DisplayName("[성공] StatusLine 객체를 생성한다")
    void StatusLine() {
        HttpVersion httpVersion = HttpVersion.HTTP_1_1;
        int statusCode = HttpStatus.FOUND.getCode();
        String statusMessage = HttpStatus.FOUND.getMessage();

        new StatusLine(httpVersion, statusCode, statusMessage);
    }

    @Test
    @DisplayName("[성공] StatusLine의 메시지를 가져온다")
    void message() {
        HttpVersion httpVersion = HttpVersion.HTTP_1_1;
        int statusCode = HttpStatus.FOUND.getCode();
        String statusMessage = HttpStatus.FOUND.getMessage();
        String message = httpVersion.name() + " " + statusCode + " " + statusMessage;

        StatusLine statusLine = new StatusLine(httpVersion, statusCode, statusMessage);

        Assertions.assertEquals(statusLine.message(), message);
    }
}
