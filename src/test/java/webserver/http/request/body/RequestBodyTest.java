package webserver.http.request.body;

import static org.junit.jupiter.api.Assertions.*;

class RequestBodyTest {

    RequestBody requestBody = new RequestBody("/user/create?userId=cih468&" +
            "password=1234&" +
            "name=%EC%B5%9C%EC%84%B1%ED%98%84&" +
            "email=cih468%40naver.com");
}