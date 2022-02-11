package webserver.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import webserver.http.connection.ConnectionIO;
import webserver.http.connection.SocketIO;
import webserver.http.message.HttpRequest;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpCodecTest {

    private static ConnectionIO socket;

    @BeforeAll
    public static void setupSocket() {
        final byte[] bytes = ("GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Content-Length: 59\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Cookie: logined=true\n" +
                "Accept: */*\n" +
                "\n" +
                "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net").getBytes(StandardCharsets.UTF_8);

        socket = mock(SocketIO.class);
        when(socket.readLine()).thenReturn(bytes);
    }

    @Test
    public void decode() {
        //given
        HttpCodec codec = new HttpCodec(socket);

        //when
        HttpRequest httpRequest = codec.decode();

        //then
    }
}