package util;

import app.db.DataBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.http.HttpRequest;
import util.http.HttpRequestUtils;
import util.http.HttpResponse;
import webserver.ServletContainer;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ServletContainerTest {

    ServletContainer servletContainer;

    @BeforeEach
    void setUp() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        servletContainer = new ServletContainer();
    }

    @Test
    void getTest() throws InvocationTargetException, IllegalAccessException, IOException, NoSuchMethodException, InstantiationException {
        String url = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\n";
        String resultHeader = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html;charset=utf-8\r\n" +
                "Content-Length: 6908\r\n" +
                "\r\n";

        HttpRequest httpRequest = new HttpRequest(url);
        HttpResponse httpResponse = new HttpResponse();
        servletContainer.service(httpRequest, httpResponse);
        assertThat(httpResponse.headerText()).isEqualTo(resultHeader);
    }

    @Test
    void postTest() throws InvocationTargetException, IllegalAccessException, IOException, NoSuchMethodException, InstantiationException {
        String request = "POST /user/create HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 93\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Accept: */*\n" +
                "\n" +
                "userId=javajigi&password=password&name=박재성&email=javajigi@slipp.net\n";

        String result = "HTTP/1.1 302 Found\r\n" +
                "Location: http://localhost:8080/index.html\r\n" +
                "\r\n";

        InputStream is = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        HttpRequest httpRequest = HttpRequestUtils.parseRequest(br);
        HttpResponse httpResponse = new HttpResponse();
        servletContainer.service(httpRequest, httpResponse);
        assertThat(httpResponse.headerText()).isEqualTo(result);

    }

    @Test
    void postObjectTest() throws InvocationTargetException, IllegalAccessException, IOException, NoSuchMethodException, InstantiationException {
        String request = "POST /user/create/v2 HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 93\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Accept: */*\n" +
                "\n" +
                "userId=javajigi&password=password&name=박재성&email=javajigi@slipp.net\n";

        String result = "HTTP/1.1 302 Found\r\n" +
                "Location: http://localhost:8080/index.html\r\n" +
                "\r\n";

        InputStream is = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        HttpRequest httpRequest = HttpRequestUtils.parseRequest(br);
        HttpResponse httpResponse = new HttpResponse();
        servletContainer.service(httpRequest, httpResponse);
        assertThat(httpResponse.headerText()).isEqualTo(result);
        assertThat(DataBase.findUserById(httpRequest.body().get("userId")).getUserId()).isEqualTo("javajigi");

    }

}
