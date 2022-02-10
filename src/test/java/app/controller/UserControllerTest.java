package app.controller;

import app.core.MyHttpServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.http.HttpRequest;
import util.http.HttpRequestUtils;
import util.http.HttpResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserControllerTest {

    private MyHttpServlet myHttpServlet;

    @BeforeEach
    void setUp() throws IOException {
        myHttpServlet = MyHttpServlet.getInstance();
        String request = "GET /user/deleteAll HTTP/1.1\n" +
                "Host: localhost:8080\n";
        HttpRequest httpRequest = makeRequest(request);
        HttpResponse httpResponse = new HttpResponse();
        myHttpServlet.service(httpRequest, httpResponse);
    }

    @Test
    void postTest() throws IOException {
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
        myHttpServlet.service(httpRequest, httpResponse);
        assertThat(httpResponse.headerText()).isEqualTo(result);

    }

    @Test
    void postObjectTest() throws IOException {
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
        myHttpServlet.service(httpRequest, httpResponse);
        assertThat(httpResponse.headerText()).isEqualTo(result);
    }

    @Test
    void userListNoLogin() throws IOException {
        String request = "GET /user/list HTTP/1.1\n" +
                "Host: localhost:8080\n";
        String result = "HTTP/1.1 302 Found\r\n" +
                "Location: http://localhost:8080/user/login\r\n" +
                "\r\n";
        HttpRequest httpRequest = makeRequest(request);
        HttpResponse httpResponse = new HttpResponse();
        myHttpServlet.service(httpRequest, httpResponse);
        // then
        assertThat(httpResponse.headerText()).isEqualTo(result);
    }


    private HttpRequest makeRequest(String request) throws IOException {
        InputStream is = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return HttpRequestUtils.parseRequest(br);
    }

}
