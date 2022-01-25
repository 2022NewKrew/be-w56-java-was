package webserver.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.request.Request;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class StaticControllerTest {

    @ParameterizedTest
    @DisplayName("url 지원 여부 확인")
    @MethodSource("getUrlSupportStream")
    void supports(String url, boolean expected) {
        //given
        final Controller controller = new StaticController();

        //when
        final boolean actual = controller.supports(url);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> getUrlSupportStream(){
        return Stream.of(
                Arguments.of("hello.html", true),
                Arguments.of("hello.ico", true),
                Arguments.of("hello.css", true),
                Arguments.of("hello.js", true),
                Arguments.of("hello.eot", true),
                Arguments.of("hello.svg", true),
                Arguments.of("hello.ttf", true),
                Arguments.of("hello.woff", true),
                Arguments.of("hello.woff2", true),
                Arguments.of("hello.png", true),
                Arguments.of("hello.min.js", true),
                Arguments.of("hello", false),
                Arguments.of("hello.java", false),
                Arguments.of("hello.c", false),
                Arguments.of("hello.jpeg", false)
        );
    }

    @ParameterizedTest
    @DisplayName("static 파일 요청 핸들링 성공")
    @MethodSource("getExistFileStream")
    void handle(String url) throws IOException {
        //given
        final Controller controller = new StaticController();
        final DataOutputStream dos = mock(DataOutputStream.class);

        final Request requestMap = new Request(url);

        final File file = new File(String.format("./webapp%s", url));
        final byte[] expectedBody = Files.readAllBytes(file.toPath());

        //when
        controller.handle(requestMap, dos);

        //then
        then(dos).should().write(
                argThat(body -> Arrays.equals(body, expectedBody)), eq(0), eq(expectedBody.length));
    }

    private static Stream<Arguments> getExistFileStream(){
        return Stream.of(
                Arguments.of("/index.html"),
                Arguments.of("/favicon.ico"),
                Arguments.of("/user/form.html"),
                Arguments.of("/qna/form.html"),
                Arguments.of("/images/80-text.png")
        );
    }
}