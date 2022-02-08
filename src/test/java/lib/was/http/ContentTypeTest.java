package lib.was.http;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContentTypeTest {

    @ParameterizedTest
    @MethodSource("provideFromExtensionParameters")
    void fromExtension(String filename, ContentType expected) {
        Optional<ContentType> result = ContentType.fromExtension(filename);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    private static Stream<Arguments> provideFromExtensionParameters() {
        return Stream.of(
                Arguments.of(".txt", ContentType.TEXT),
                Arguments.of(".html", ContentType.HTML),
                Arguments.of(".css", ContentType.CSS),
                Arguments.of(".js", ContentType.JAVASCRIPT),
                Arguments.of(".png", ContentType.PNG)
        );
    }
}
