package webserver;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestMapperTest {

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        SingletonRegistry.registerClassesInPackage("testController");
        setFinalStatic(
                RequestMapper.class.getDeclaredField("controllers"),
                IOUtils.findAllClassesInPackage("testController"));
    }

    void setFinalStatic(Field field, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }

    @Test
    void testGetMethod() throws IOException {
        String requestString = "GET /test HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: curl/7.64.1\n" +
                "Accept: */*";
        HttpResponse response = fromRequestString(requestString);
        byte[] content = "GET method called".getBytes();
        assertEquals(response.getStatus(), HttpStatus.OK);
        assertArrayEquals(response.getBody(), content);
    }

    private HttpResponse fromRequestString(String requestString) throws IOException {
        InputStream in = new ByteArrayInputStream(requestString.getBytes());
        HttpRequest request = HttpRequest.from(in);
        RequestMapper mapper = new RequestMapper(request);
        return mapper.getResponse();
    }

    @Test
    void testPostMethod() throws IOException {
        String requestString = "POST /test HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: curl/7.64.1\n" +
                "Accept: */*\n" +
                "\n" +
                "Something in body";
        HttpResponse response = fromRequestString(requestString);
        byte[] content = "POST method called".getBytes();
        assertEquals(response.getStatus(), HttpStatus.Found);
        assertArrayEquals(response.getBody(), content);
    }

    @Test
    void testUriQuery() throws IOException {
        String requestString = "GET /test/query?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*";
        HttpResponse response = fromRequestString(requestString);
        Map<String, String> query = new HashMap<>();
        query.put("userId", "javajigi");
        query.put("password", "password");
        query.put("name", "박재성");
        query.put("email", "javajigi@slipp.net");
        byte[] content = query.toString().getBytes();
        assertEquals(response.getStatus(), HttpStatus.OK);
        assertArrayEquals(response.getBody(), content);
    }

    @Test
    void testBody() throws IOException {
        String requestString = "POST /test/body HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 93\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Accept: */*\n" +
                "\n" +
                "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";
        HttpResponse response = fromRequestString(requestString);
        byte[] content = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net".getBytes();
        assertEquals(response.getStatus(), HttpStatus.OK);
        assertArrayEquals(response.getBody(), content);
    }

    @Test
    void testStaticFile() throws IOException {
        String requestString = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: curl/7.64.1\n" +
                "Accept: */*";
        HttpResponse response = fromRequestString(requestString);
        byte[] content = Files.readAllBytes(Path.of("webapp/index.html"));
        assertEquals(response.getStatus(), HttpStatus.OK);
        assertArrayEquals(response.getBody(), content);
    }

    @Test
    void testNotFound() throws IOException {
        String requestString = "GET /unknown/uri?for=testing&not=found HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: curl/7.64.1\n" +
                "Accept: */*";
        HttpResponse response = fromRequestString(requestString);
        assertEquals(response.getStatus(), HttpStatus.NotFound);
    }
}
