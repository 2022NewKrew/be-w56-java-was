package webserver.http.request;

import org.junit.jupiter.api.Test;
import webserver.http.request.body.RequestBody;
import webserver.http.request.header.RequestHeader;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

    RequestHeader requestHeader = new RequestHeader("POST " +
            "/user/create HTTP/1.1\n" +
            "Host: localhost:8080\n" +
            "Connection: keep-alive\n" +
            "Content-Length: 85\n" +
            "Content-Type: application/x-www-form-urlencoded\n" +
            "Accept: */*");

    RequestBody requestBody = new RequestBody("/user/create?userId=cih468&" +
            "password=1234&" +
            "name=%EC%B5%9C%EC%84%B1%ED%98%84&" +
            "email=cih468%40naver.com");

    InfoMap infoMap = new InfoMap(requestHeader, requestBody);

    HttpRequest httpRequest = new HttpRequest(requestHeader, requestBody, infoMap);

    @Test
    void getUrlPath() {
        assertEquals("/user/create", httpRequest.getUrlPath());
    }
}