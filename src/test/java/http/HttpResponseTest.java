package http;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpResponseTest {

    @Test
    void testStatus200() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            HttpResponse response = new HttpResponse(HttpStatus.OK);
            DataOutputStream dos = new DataOutputStream(out);
            response.send(dos);
            String actual = "HTTP/1.1 200 OK\r\n\r\n";
            assertEquals(out.toString(),actual);
        }
    }

    @Test
    void testStringBodyWithStatus200() throws IOException {
        String body = "Test string";
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            HttpResponse response = new HttpResponse(body.getBytes(), HttpStatus.OK);
            DataOutputStream dos = new DataOutputStream(out);
            response.send(dos);
            String actual = "HTTP/1.1 200 OK\r\n" +
                    "Content-Length: 11\r\n\r\n" +
                    "Test string";
            assertEquals(out.toString(), actual);
        }
    }

    @Test
    void testStatus301() throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            HttpResponse response = new HttpResponse(headers, HttpStatus.Found);
            DataOutputStream dos = new DataOutputStream(out);
            response.send(dos);
            String actual = "HTTP/1.1 301 Found\r\n" +
                    "Location: /index.html\r\n\r\n";
            assertEquals(out.toString(), actual);
        }
    }
}
