package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class RequestParserTest {

    @ParameterizedTest
    @DisplayName("http request url 파싱 테스트")
    @MethodSource("getHttpRequestStringStream")
    void parseUrl(List<String> requestStrings) throws IOException {
        //given
        final RequestParser requestParser = new RequestParser();
        final BufferedReader br = createMockBufferedReader(requestStrings);

        //when
        final RequestMap requestMap = requestParser.parse(br);

        //then
        final String requestLine = getRequestLine(requestStrings);
        assertThat(requestLine).contains((CharSequence) requestMap.getHeader("url").orElseThrow());
    }



    private static BufferedReader createMockBufferedReader(List<String> requestStrings) throws IOException {
        final BufferedReader br = mock(BufferedReader.class);
        final String requestLine = getRequestLine(requestStrings);

        if(requestStrings.size() == 1) {
            given(br.readLine()).willReturn(requestLine);
        }

        if(requestStrings.size() >= 2){
            String[] linesAfterRequestLine = requestStrings.subList(1, requestStrings.size()).toArray(String[]::new);
            given(br.readLine()).willReturn(requestLine, linesAfterRequestLine);
        }

        return br;
    }

    private static String getRequestLine(List<String> httpRequestStrings){
        return httpRequestStrings.get(0);
    }

    private static Stream<Arguments> getHttpRequestStringStream(){
        return getHttpRequestList().stream()
                .map(Arguments::of);
    }

    private static List<List<String>> getHttpRequestList(){
        return List.of(
                List.of(
                        "GET /index.html HTTP/1.1",
                        "Host: localhost:8080",
                        "Connection: keep-alive",
                        "Accept: */*",
                        RequestParser.BLANK_LINE
                ),
                List.of(
                        "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1",
                        "Host: localhost:8080",
                        "Connection: keep-alive",
                        "Accept: */*",
                        RequestParser.BLANK_LINE
                )
        );
    }
}