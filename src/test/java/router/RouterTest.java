package router;

import model.Request;
import model.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RouterTest {

    @Test
    @DisplayName("/index로 입력시 /index.html로 라우팅 테스트")
    void routing_default() {
        Request testRequest = Request.builder().
                requestLine( new String[]{"GET", "/index", "HTTP/1.1"})
                .header(Collections.singletonMap("Accept", "text"))
                .build();

        Response testResponse = Router.routing(testRequest);
        assertEquals("/index.html", testResponse.getFilePath());
    }

    @Test
    @DisplayName("/user/create로 입력시 /user/list.html로 라우팅 테스트")
    void routing_user_create() {
        Map<String, String> testQueryMap = new HashMap<>();
        testQueryMap.put("userId", "wook961206");
        testQueryMap.put("password","123");
        testQueryMap.put("name", "muscle");
        testQueryMap.put("email", "wook961206@naver.com");

        Request testRequest = Request.builder().
                requestLine( new String[]{"GET", "/user/create", "HTTP/1.1"})
                .header(Collections.singletonMap("Accept", "text"))
                .queryString(testQueryMap)
                .build();

        Response testResponse = Router.routing(testRequest);
        assertEquals("/user/list.html", testResponse.getFilePath());
    }
}