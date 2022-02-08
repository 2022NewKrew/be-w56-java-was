package mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.request.HttpRequest;
import webserver.request.HttpRequestBody;
import webserver.request.HttpRequestHeader;
import webserver.request.HttpRequestLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class UrlMapperTest {
    @DisplayName("url request를 넘겼을 때 전달되어야하는 Response 결과 테스트")
    @ParameterizedTest
    @MethodSource("arguments")
    void mappingResult(String line, List<String> header, String body, String result) {
        try {
            HttpRequestLine httpRequestLine = HttpRequestLine.makeHttpRequestLine(line);
            HttpRequestHeader httpRequestHeader = HttpRequestHeader.makeHttpRequestHeader(header);
            HttpRequestBody httpRequestBody = HttpRequestBody.makeHttpRequestBody(body.toCharArray());

            HttpRequest httpRequest = new HttpRequest(httpRequestLine, httpRequestHeader, httpRequestBody);

            UrlMapper urlMapper = new UrlMapper();

            ResponseSendDataModel responseSendDataModel = urlMapper.mappingResult(httpRequest);

            assertThat(responseSendDataModel.getName()).isEqualTo(result);
        } catch (IOException e) {}
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("GET / HTTP/1.1", new ArrayList<>(), "", "/index.html"),
                Arguments.of("GET /users/form HTTP/1.1", new ArrayList<>(), "", "/user/form.html"),
                Arguments.of("GET /users/login HTTP/1.1", new ArrayList<>(), "", "/user/login.html"),
                Arguments.of("GET /asdgwegwe HTTP/1.1", new ArrayList<>(), "", "/404_error.html"),
                Arguments.of("POST /users/form HTTP/1.1", new ArrayList<>(), "userId=aa&password=aa&name=aa&email=aa", "redirect:/"),
                Arguments.of("POST /users/login HTTP/1.1", new ArrayList<>(), "userId=aa&password=aa", "redirect:/"),
                Arguments.of("POST /users/login HTTP/1.1", new ArrayList<>(), "userId=aaa&password=aa", "redirect:/users/login_failed")
        );
    }
}
