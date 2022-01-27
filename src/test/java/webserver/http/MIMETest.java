package webserver.http;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


class MIMETest {

    @ParameterizedTest
    @MethodSource("provideAllRequestURIForMIME")
    void testMimeHtml(String uri, MIME expectMIME, boolean isNone, boolean isStatic) {
        MIME result = MIME.from(uri);
        assertThat(result).isEqualTo(expectMIME);
        assertThat(result == MIME.NONE).isEqualTo(isNone);
        assertThat(result.isStaticResource()).isEqualTo(isStatic);
    }

    private static Stream<Arguments> provideAllRequestURIForMIME() {
        return Stream.of(
                Arguments.of("/foo.html", MIME.HTML, false, true),
                Arguments.of("/foo.css", MIME.CSS, false, true),
                Arguments.of("/foo.jpg", MIME.JPEG, false, true),
                Arguments.of("/foo.png", MIME.PNG, false, true),
                Arguments.of("/foo.js", MIME.JS, false, true),
                Arguments.of("/foo.ico", MIME.ICO, false, true),
                Arguments.of("/foo.ttf", MIME.TTF, false, true),
                Arguments.of("/foo.woff", MIME.WOFF, false, true),
                Arguments.of("/foo.bar", MIME.NONE, true, false)
        );
    }

}
