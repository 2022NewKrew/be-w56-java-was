package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BodyTest {

    private final String testBody = "testBody";

    @Test
    @DisplayName("[성공] Body 객체를 생성한다")
    void of() {
        Body body = Body.of(testBody);
    }

    @Test
    @DisplayName("[성공] get으로 body 값을 받아올 수 있다")
    void get() {
        Body body = Body.of(testBody);

        Assertions.assertEquals(body.get(), testBody);
    }
}
