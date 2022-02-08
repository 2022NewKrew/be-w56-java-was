package http;

import http.header.Cookie;
import http.message.ResponseMessage;
import http.startline.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ResponseMessageTest {

    @Disabled
    @Test
    void createFailed_WhenNull() {
//        assertThatThrownBy(() -> ResponseMessage.create(HttpStatus.OK, (byte[]) null))
//                .isInstanceOf(IllegalArgumentException.class);
//
//        assertThatThrownBy(() -> ResponseMessage.create(null, (byte[]) null))
//                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> ResponseMessage.create(null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Disabled
    @Test
    void createSuccess() {
        //ResponseMessage.create(HttpStatus.OK, new byte[]{});
        ResponseMessage.create(HttpStatus.FOUND, "/", Cookie.parse(null));
    }
}
