package http;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VersionTest {

    @ParameterizedTest
    @MethodSource("provideFromStringParameters")
    void fromString(String version, Version expected) {
        Optional<Version> result = Version.fromString(version);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    private static Stream<Arguments> provideFromStringParameters() {
        return Stream.of(
                Arguments.of("HTTP/1.0", Version.HTTP_1_0),
                Arguments.of("HTTP/1.1", Version.HTTP_1_1)
        );
    }
}
