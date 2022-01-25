package http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class HeadersTest {

    private static Map<FieldName, FieldValue> map;

    @BeforeAll
    @Test
    static void setUp() {
        map = new HashMap<>();
        map.put(new FieldName("Host"), new FieldValue("localhost:8080"));
        map.put(new FieldName("Connection"), new FieldValue("keep-alive"));
        map.put(new FieldName("Cache-Control"), new FieldValue("max-age=0"));
    }

    @Test
    void equalsRequestHeader() {
        assertThat(new Headers(map)).isEqualTo(new Headers(map));
    }

    @Test
    void notEqualsHeaders_WhenKeySetDifferent() {
        Map<FieldName, FieldValue> compare = new HashMap<>();
        compare.put(new FieldName("Host"), new FieldValue("localhost:8080"));
        compare.put(new FieldName("Connection"), new FieldValue("keep-alive"));

        assertThat(new Headers(map)).isNotEqualTo(new Headers(compare));
    }

    @Test
    void notEqualsHeaders_WhenValueDifferent() {
        Map<FieldName, FieldValue> compare = new HashMap<>();
        compare.put(new FieldName("Host"), new FieldValue("www.daum.net"));
        compare.put(new FieldName("Connection"), new FieldValue("keep-alive"));
        compare.put(new FieldName("Cache-Control"), new FieldValue("max-age=0"));

        assertThat(new Headers(map)).isNotEqualTo(new Headers(compare));
    }
}
