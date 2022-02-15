package webserver.http.request.body;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BodyMapTest {

    BodyMap emptyBodyMap = new BodyMap("");
    BodyMap bodyMap = new BodyMap("userId=cih468&" +
            "password=1234&" +
            "name=%EC%B5%9C%EC%84%B1%ED%98%84&" +
            "email=cih468%40naver.com");

    @Test
    public void containsKey() {
        assertFalse(emptyBodyMap.containsKey("userId"));
        assertTrue(bodyMap.containsKey("userId"));
    }

    @Test
    public void get() {
        assertEquals("cih468", bodyMap.get("userId"));
    }


}