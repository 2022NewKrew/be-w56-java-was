package http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpHeadersTest {
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
    }

    @Test
    void testAddInvalidFormatDoNothing() {
        headers.add("There is not semicolon");
        assertEquals(headers.size(), 0);
    }

    @Test
    void testAddAndGet() {
        headers.add("Content-Length: 8");
        headers.add("Content-Type: text/html");
        assertEquals(headers.get("Content-Length"), "8");
        assertEquals(headers.get("Content-Type"), "text/html");
    }
}
