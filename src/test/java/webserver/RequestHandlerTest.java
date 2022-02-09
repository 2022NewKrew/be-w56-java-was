package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import dao.UserDao;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayName("RequestHandler 테스트")
class RequestHandlerTest {

    @DisplayName("GET /index.html 테스트")
    @Test
    void run1() throws IOException {
        //give
        Socket socket = Mockito.mock(Socket.class);
        String request = "GET /index.html HTTP/1.1\r\n"
                + "testHeader: testHeaderValue\r\n"
                + "\r\n";
        OutputStream os = new ByteArrayOutputStream();
        Mockito.when(socket.getInputStream())
                .thenReturn(new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8)));
        Mockito.when(socket.getOutputStream())
                .thenReturn(os);
        RequestHandler handler = new RequestHandler(socket);
        //when
        handler.run();
        //then
        assertThat(os.toString()).contains("200", "OK", "HTTP/1.1", "Content-Type",
                "text/html", "Content-Length");
    }

    @DisplayName("POST /user/create 테스트")
    @Test
    void run2() throws IOException {
        //give
        Socket socket = Mockito.mock(Socket.class);
        String request = "POST /user/create HTTP/1.1\r\n"
                + "Content-Length: 53\r\nheaderKey1: headerValue1\r\nheaderKey2: headerValue2\r\n"
                + "\r\n"
                + "userId=userId&password=password&name=name&email=email";
        OutputStream os = new ByteArrayOutputStream();
        Mockito.when(socket.getInputStream())
                .thenReturn(new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8)));
        Mockito.when(socket.getOutputStream())
                .thenReturn(os);
        RequestHandler handler = new RequestHandler(socket);
        //when
        handler.run();
        //then
        assertThat(os.toString()).contains("302", "Found", "HTTP/1.1", "Location");

        UserDao.getInstance().delete(UserDao.getInstance().findByUserId("userId").getId());
    }

    @DisplayName("POST /user/login 테스트")
    @Test
    void run3() throws IOException {
        //give
        Socket socket = Mockito.mock(Socket.class);
        String request = "POST /user/login HTTP/1.1\r\n"
                + "Content-Length: 31\r\nheaderKey1: headerValue1\r\nheaderKey2: headerValue2\r\n"
                + "\r\n"
                + "userId=userId&password=password";
        OutputStream os = new ByteArrayOutputStream();
        Mockito.when(socket.getInputStream())
                .thenReturn(new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8)));
        Mockito.when(socket.getOutputStream())
                .thenReturn(os);
        RequestHandler handler = new RequestHandler(socket);
        //when
        handler.run();
        //then
        assertThat(os.toString()).contains("401", "Unauthorized", "Content-Type", "Content-Length");
    }

    @DisplayName("GET /user/list.html 테스트")
    @Test
    void run4() throws IOException {
        //give
        Socket socket = Mockito.mock(Socket.class);
        String request = "GET /user/list.html HTTP/1.1\r\n"
                + "Cookie: logined=true\r\nheaderKey1: headerValue1\r\nheaderKey2: headerValue2\r\n"
                + "\r\n"
                + "userId=userId&password=password";
        OutputStream os = new ByteArrayOutputStream();
        Mockito.when(socket.getInputStream())
                .thenReturn(new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8)));
        Mockito.when(socket.getOutputStream())
                .thenReturn(os);
        RequestHandler handler = new RequestHandler(socket);
        //when
        handler.run();
        //then
        assertThat(os.toString()).contains("200", "OK", "Content-Type", "Content-Length");
    }

    @DisplayName("GET / 테스트")
    @Test
    void run5() throws IOException {
        //give
        Socket socket = Mockito.mock(Socket.class);
        String request = "GET / HTTP/1.1\r\n"
                + "headerKey1: headerValue1\r\nheaderKey2: headerValue2\r\n"
                + "\r\n";
        OutputStream os = new ByteArrayOutputStream();
        Mockito.when(socket.getInputStream())
                .thenReturn(new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8)));
        Mockito.when(socket.getOutputStream())
                .thenReturn(os);
        RequestHandler handler = new RequestHandler(socket);
        //when
        handler.run();
        //then
        assertThat(os.toString()).contains("302", "Found", "Location", "/index.html");
    }

    @DisplayName("Bad Request 테스트")
    @Test
    void runBadRequest() throws IOException {
        //give
        Socket socket = Mockito.mock(Socket.class);
        String request = "POST /user/login HTTP/1.1\r\n"
                + "Content-Length: 31\r\nheaderKey1: headerValue1\r\nheaderKey2: headerValue2\r\n"
                + "\r\n"
                + "userId=&password=password";
        OutputStream os = new ByteArrayOutputStream();
        Mockito.when(socket.getInputStream())
                .thenReturn(new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8)));
        Mockito.when(socket.getOutputStream())
                .thenReturn(os);
        RequestHandler handler = new RequestHandler(socket);
        //when
        handler.run();
        //then
        assertThat(os.toString()).contains("400", "Bad Request", "Content-Type", "Content-Length");
    }

    @DisplayName("Not Found 테스트")
    @Test
    void runNotFound() throws IOException {
        //give
        Socket socket = Mockito.mock(Socket.class);
        String request = "GET /neverExist.html HTTP/1.1\r\n"
                + "testHeader: testHeaderValue\r\n"
                + "\r\n";
        OutputStream os = new ByteArrayOutputStream();
        Mockito.when(socket.getInputStream())
                .thenReturn(new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8)));
        Mockito.when(socket.getOutputStream())
                .thenReturn(os);
        RequestHandler handler = new RequestHandler(socket);
        //when
        handler.run();
        //then
        assertThat(os.toString()).contains("404", "Not Found", "Content-Type", "Content-Length");
    }
}
