package webserver.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.request.HttpRequest;
import util.request.HttpVersion;
import util.request.MethodType;
import util.response.HttpResponse;
import util.response.HttpStatus;
import webserver.controller.common.StaticController;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class StaticControllerTest {

    @ParameterizedTest
    @DisplayName("url 지원 여부 확인")
    @MethodSource("getUrlSupportStream")
    void supports(MethodType methodType, String url, boolean expected) {
        //given
        final Controller controller = new StaticController();
        final HttpRequest httpRequest = HttpRequest.builder()
                .method(methodType)
                .url(url)
                .build();

        //when
        final boolean actual = controller.supports(httpRequest);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> getUrlSupportStream(){
        return Stream.of(
                Arguments.of(MethodType.GET,"hello.html", true),
                Arguments.of(MethodType.GET,"hello.ico", true),
                Arguments.of(MethodType.GET,"hello.css", true),
                Arguments.of(MethodType.GET,"hello.js", true),
                Arguments.of(MethodType.GET,"hello.eot", true),
                Arguments.of(MethodType.GET,"hello.svg", true),
                Arguments.of(MethodType.GET,"hello.ttf", true),
                Arguments.of(MethodType.GET,"hello.woff", true),
                Arguments.of(MethodType.GET,"hello.woff2", true),
                Arguments.of(MethodType.GET,"hello.png", true),
                Arguments.of(MethodType.GET,"hello.min.js", true),
                Arguments.of(MethodType.GET,"hello", false),
                Arguments.of(MethodType.GET,"hello.java", false),
                Arguments.of(MethodType.GET,"hello.c", false),
                Arguments.of(MethodType.POST,"hello.html", false),
                Arguments.of(MethodType.PUT,"hello.html", false),
                Arguments.of(MethodType.DELETE,"hello.html", false)
        );
    }

    @ParameterizedTest
    @DisplayName("static 파일 요청 핸들링 성공")
    @MethodSource("getExistFileStream")
    void handle(String url) throws IOException {
        //given
        final Controller controller = new StaticController();
        final DataOutputStream dos = mock(DataOutputStream.class);

        final HttpRequest httpRequest = HttpRequest.builder()
                .method(MethodType.GET)
                .url(url)
                .httpVersion(HttpVersion.VERSION_1_1)
                .build();

        //when
        HttpResponse httpResponse = (HttpResponse) controller.doHandle(httpRequest);

        //then
        assertThat(httpResponse.getStatus()).isEqualTo(HttpStatus.SUCCESS);
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