package http;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ResponseMessageTest {

    @Test
    void createFailed_WhenNull() {
//        assertThatThrownBy(()-> new ResponseMessage(null, null, null))
//                .isInstanceOf(IllegalArgumentException.class);
//
//        assertThatThrownBy(()-> new ResponseMessage(null, new Headers(new HashMap<>()), null))
//                .isInstanceOf(IllegalArgumentException.class);
//
//        assertThatThrownBy(()-> new ResponseMessage(new StatusLine(HttpVersion.V_1_1, HttpStatus.OK), null, null))
//                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createSuccess() {
//        new ResponseMessage(new StatusLine(HttpVersion.V_1_1, HttpStatus.OK), new Headers(new HashMap<>()), null);
    }
}
