package model;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.BadRequestFormatException;
import exceptions.InvalidHttpMethodException;
import exceptions.InvalidHttpVersionException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class StartLineTest {

    @DisplayName("[성공] startLine 객체를 생성한다")
    @ParameterizedTest(name = "startLineMessage = {0}")
    @ValueSource(strings = {"GET /index.html HTTP/1.1", "   GET /index.html HTTP/1.1   "})
    void of(String startLineMessage) {

        StartLine startLine = StartLine.of(startLineMessage);
    }

    @DisplayName("[실패] 인자 개수가 3개가 아닌 경우")
    @ParameterizedTest(name = "startLineMessage = {0}")
    @ValueSource(strings = {"/index.html HTTP/1.1", "GET POST /index.html HTTP/1.1", "GET"})
    void of_FailedBy_WrongNumberOfToken(String startLineMessage) {
        Assertions.assertThrows(BadRequestFormatException.class,
                () -> StartLine.of(startLineMessage));
    }

    @DisplayName("[실패] 없는 http 버젼을 작성한 경우")
    @ParameterizedTest(name = "startLineMessage = {0}")
    @ValueSource(strings = {"GET /index.html HTTP/2.0", "GET /index.html HTTP/666"})
    void of_FailedBy_InvalidHttpVersion(String startLineMessage) {
        Assertions.assertThrows(InvalidHttpVersionException.class,
                () -> StartLine.of(startLineMessage));
    }

    @DisplayName("[실패] 없는 http 메소드을 작성한 경우")
    @ParameterizedTest(name = "method = {0}")
    @ValueSource(strings = {"AAA /index.html HTTP/1.1", "GETT /index.html HTTP/1.1"})
    void of_FailedBy_InvalidHttpMethod(String startLineMessage) {
        Assertions.assertThrows(InvalidHttpMethodException.class,
                () -> StartLine.of(startLineMessage));
    }

    @Test
    @DisplayName("[실패] startLine이 빈 문자열인 경우")
    void of_FailedBy_EmptyString() {
        String startLineMessage = "";

        Assertions.assertThrows(BadRequestFormatException.class,
                () -> StartLine.of(startLineMessage));
    }

    // TODO - 아래 테스트 추후 구현
    @Test
    @Disabled
    void getHttpMethod() {
    }

    @Test
    @Disabled
    void getUrl() {
    }

    @Test
    @Disabled
    void getQuery() {
    }
}
