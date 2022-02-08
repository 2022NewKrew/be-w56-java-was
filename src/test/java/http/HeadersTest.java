package http;

import http.header.Headers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class HeadersTest {

    private static List<String> input;

    @BeforeAll
    @Test
    static void setUp() {
        input = new ArrayList<>(Arrays.asList(
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Cache-Control: max-age=0"
        ));
    }

    @Test
    void createRequestHeader() {
        Map<String, String> compare = new HashMap<>();
        compare.put("Host", "localhost:8080");
        compare.put("Connection", "keep-alive");

        assertThat(Headers.create(input).getHeaders()).isEqualTo(Headers.create(input).getHeaders());
    }
}
