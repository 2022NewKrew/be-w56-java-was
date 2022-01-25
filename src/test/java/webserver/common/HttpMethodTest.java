package webserver.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpMethodTest {

    @Test
    @DisplayName("Enum 테스트")
    void testEnum() throws Exception {
        // given

        // when
        String findString1 = "get";
        String findString2 = "post";
        String findString3 = "delete";

        // then
        assertEquals(HttpMethod.findMethod(findString1), HttpMethod.GET);
        assertEquals(HttpMethod.findMethod(findString2), HttpMethod.POST);
        assertEquals(HttpMethod.findMethod(findString3), HttpMethod.UNKNOWN);

    }

}
