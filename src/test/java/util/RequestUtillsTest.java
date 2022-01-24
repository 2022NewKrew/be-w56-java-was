package util;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestUtillsTest {

    @Test
    void readUrlPath() throws IOException {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        when(bufferedReader.readLine()).
                thenReturn("GET /index.html HTTP/1.1");

        String url = RequestUtills.readUrlPath(bufferedReader);
        assertEquals("/index.html", url);
    }

    @Test
    void readHeader() throws IOException {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        when(bufferedReader.readLine()).
                thenReturn("Host: localhost").
                thenReturn("Connection: keep-alive").
                thenReturn("Cookie: JSESSIONID=1234567").
                thenReturn("");

        Map<String, String> testMap = RequestUtills.readHeader(bufferedReader);

        assertEquals("localhost", testMap.get("Host"));
        assertEquals("keep-alive", testMap.get("Connection"));
        assertEquals("JSESSIONID=1234567", testMap.get("Cookie"));
    }
}