package http;

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
    void equalsRequestHeader() {
        assertThat(Headers.create(input)).isEqualTo(Headers.create(input));
    }

    @Test
    void notEqualsHeaders_WhenKeySetDifferent() {
        Map<String, String> compare = new HashMap<>();
        compare.put("Host", "localhost:8080");
        compare.put("Connection", "keep-alive");

        assertThat(Headers.create(input)).isNotEqualTo(Headers.create(compare));
    }

    @Test
    void notEqualsHeaders_WhenValueDifferent() {
        Map<String, String> compare = new HashMap<>();
        compare.put("Host", "www.daum.net");
        compare.put("Connection", "keep-alive");
        compare.put("Cache-Control", "max-age=0");

        assertThat(Headers.create(input)).isNotEqualTo(Headers.create(compare));
    }
}
