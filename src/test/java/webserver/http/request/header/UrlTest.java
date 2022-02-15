package webserver.http.request.header;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlTest {

    Url url = new Url("GET " +
            "/user/create?userId=cih468&password=1234&name=%EC%B5%9C%EC%84%B1%ED%98%84&email=cih468%40naver.com" +
            " HTTP/1.1\n" +
            "Host: localhost:8080\n" +
            "Connection: keep-alive\n" +
            "Accept: */*");

    @Test
    void getPath() {
        assertEquals("/user/create", url.getPath());
    }

    @Test
    void getFullPath() {
        assertEquals("/user/create?" + "userId=cih468&" +
                "password=1234&" +
                "name=%EC%B5%9C%EC%84%B1%ED%98%84&" +
                "email=cih468%40naver.com", url.getFullPath());
    }
}