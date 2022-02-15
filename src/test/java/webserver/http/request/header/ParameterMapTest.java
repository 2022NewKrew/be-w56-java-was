package webserver.http.request.header;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParameterMapTest {

    ParameterMap parameterMapByGetMethod = new ParameterMap(
            "/user/create?userId=cih468" +
                    "&password=1234" +
                    "&name=%EC%B5%9C%EC%84%B1%ED%98%84" +
                    "&email=cih468%40naver.com"
    );
    ParameterMap parameterMapByPostMethod = new ParameterMap("/user/create");


    @Test
    void containsKey() {
        assertTrue(parameterMapByGetMethod.containsKey("userId"));
        assertFalse(parameterMapByPostMethod.containsKey("userId"));
    }

    @Test
    void get() {
        assertEquals("cih468", parameterMapByGetMethod.get("userId"));
    }
}