package webserver.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import webserver.controller.request.HttpRequest;
import webserver.controller.request.RequestLine;

class ServiceTypeTest {

    @Test
    @DisplayName("ServiceType 선택 테스트")
    void testServiceType() throws Exception {
        // given
        HttpRequest httpRequest1 = HttpRequest.of(RequestLine.from("GET / HTTP/1.1"), null, null);
        HttpRequest httpRequest2 = HttpRequest.of(RequestLine.from("GET /index.html?hello=23&hi=40 HTTP/1.1"), null, null);
        HttpRequest httpRequest3 = HttpRequest.of(RequestLine.from("GET /css/styles.css HTTP/1.1"), null, null);
        HttpRequest httpRequest4 = HttpRequest.of(RequestLine.from("GET /wrongPath HTTP/1.1"), null, null);
        HttpRequest httpRequest5 = HttpRequest.of(RequestLine.from("GEasdfT / HTTP/1.1"), null, null);

        // when
        ServiceType serviceType1 = ServiceType.findService(httpRequest1);
        ServiceType serviceType2 = ServiceType.findService(httpRequest2);
        ServiceType serviceType3 = ServiceType.findService(httpRequest3);
        ServiceType serviceType4 = ServiceType.findService(httpRequest4);
        ServiceType serviceType5 = ServiceType.findService(httpRequest5);

        // then
        assertEquals(serviceType1, ServiceType.GET_HOME);
        assertEquals(serviceType2, ServiceType.GET_STATIC);
        assertEquals(serviceType3, ServiceType.GET_STATIC);
        assertEquals(serviceType4, ServiceType.GET_STATIC);
        assertEquals(serviceType5, ServiceType.ERR_405);

    }

}
