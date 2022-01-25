package http;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocatorTest {

    @ParameterizedTest
    @MethodSource("provideParseParameters")
    void parse(String locator, String path, Map<String, String> query, String fragment) {
        Locator result = Locator.parse(locator);

        assertEquals(new Locator(path, query, fragment), result);
    }

    private static Stream<Arguments> provideParseParameters() {
        return Stream.of(
                Arguments.of(
                        "/index.html?foo=b%20a%20r&baz=qux#hash",
                        "/index.html",
                        Map.of("foo", "b a r", "baz", "qux"),
                        "hash"
                ),
                Arguments.of(
                        "/index.html?foo=bar",
                        "/index.html",
                        Collections.singletonMap("foo", "bar"),
                        ""
                ),
                Arguments.of(
                        "/index.html#hash",
                        "/index.html",
                        Collections.emptyMap(),
                        "hash"
                ),
                Arguments.of(
                        "/index.html",
                        "/index.html",
                        Collections.emptyMap(),
                        ""
                )
        );
    }
}
