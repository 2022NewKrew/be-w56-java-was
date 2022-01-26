package util.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class HttpHttpRequestReaderTest {

    @ParameterizedTest
    @DisplayName("http request url 파싱 테스트")
    @MethodSource("getRequestStringStream")
    void parseUrl(String requestString) throws IOException {
        //given
        final InputStream inputStream = toInputStream(requestString);
        final HttpRequestReader httpRequestReader = new HttpRequestReader(inputStream);

        //when
        final HttpRequest httpRequest = httpRequestReader.read();

        //then
        final String requestLine = getRequestLine(requestString);
        assertThat(requestLine).contains(httpRequest.getUrl());
    }

    @ParameterizedTest
    @DisplayName("http request queryString 파싱 테스트")
    @MethodSource("getRequestStringStream")
    void parseQueryString(String requestString) throws IOException{
        //gvien
        final InputStream inputStream = toInputStream(requestString);
        final HttpRequestReader httpRequestReader = new HttpRequestReader(inputStream);

        //when
        final HttpRequest httpRequest = httpRequestReader.read();

        //then
        final String queryString = getRequestLine(requestString);
        httpRequest.getQueryParams().forEach((k, v) -> {
            assertThat(queryString).contains(k);
            assertThat(queryString).contains(v);
        });
    }

    private static InputStream toInputStream(String string){
        return  new ByteArrayInputStream(string.getBytes());
    }

    private static String getRequestLine(String httpRequestStrings){
        return httpRequestStrings.split("\r\n")[0];
    }

    private static Stream<Arguments> getRequestStringStream(){
        return Stream.of(
                "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Accept: */*\r\n" +
                        "",
                "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Accept: */*\r\n" +
                        "")
                .map(Arguments::of);
    }
}