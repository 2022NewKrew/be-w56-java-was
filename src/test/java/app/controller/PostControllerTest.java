package app.controller;

import app.core.MyHttpServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.http.HttpRequest;
import util.http.HttpResponse;
import util.http.HttpStatus;
import webserver.HttpServlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostControllerTest {
    private HttpServlet myHttpServlet;

    @BeforeEach
    void setUp() throws NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        myHttpServlet = new MyHttpServlet();
    }

    @Test
    void postsTest() throws IOException {
        String request = "GET /posts HTTP/1.1\n" +
                "Host: localhost:8080\n";
        HttpRequest httpRequest = RequestBuilder.makeRequest(request);
        HttpResponse httpResponse = new HttpResponse();
        myHttpServlet.service(httpRequest, httpResponse);
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void insertPostTest() throws IOException {
        String request = "POST /post HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 37\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Accept: */*\n" +
                "\n" +
                "content=tester tester 3&writer=tester\n";

        HttpRequest httpRequest = RequestBuilder.makeRequest(request);
        HttpResponse httpResponse = new HttpResponse();
        myHttpServlet.service(httpRequest, httpResponse);
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
    }


}
