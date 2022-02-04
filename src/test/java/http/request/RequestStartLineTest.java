package http.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("RequestStartLine 테스트")
class RequestStartLineTest {

    @DisplayName("stringToRequestLine 메서드는 올바른 startLineString 을 입력받았을 때 RequestStartLine r을 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "GET /test HTTP/1.1",
            "POST /otherTest HTTP/2",
            "DELETE /lastTest HTTP/3"
    })
    void stringToRequestLine(String testStartLines) {
        //give
        List<String> components = List.of(testStartLines.split(" "));
        //when
        RequestStartLine startLine = RequestStartLine.from(testStartLines);
        //then
        assertThat(components.get(0)).isEqualTo(startLine.getMethod().toString());
        assertThat(components.get(1)).isEqualTo(startLine.getUrl());
    }

    @DisplayName("stringToRequestLine 메서드는 올바른 startLineString(query 포함) 을 입력받았을 때 RequestStartLine 을 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "GET /test?test=test HTTP/1.1",
            "POST /otherTest?query=query&test=test HTTP/2",
            "DELETE /lastTest?moko=ko&query=query&test=test HTTP/3"
    })
    void stringToRequestLineWithQueryString(String testStartLines) {
        //give
        List<String> components = List.of(testStartLines.split(" "));
        String testMethod = components.get(0);
        String testUrl = components.get(1).split("\\?")[0];
        List<String> testQuery = List.of(
                components.get(1).split("\\?")[1].split("&")[0].split("="));
        String testKey = testQuery.get(0);
        String testValue = testQuery.get(1);
        //when
        RequestStartLine startLine = RequestStartLine.from(testStartLines);
        Map<String, String> queries = startLine.getQuery();
        //then
        assertThat(startLine.getMethod().toString()).isEqualTo(testMethod);
        assertThat(startLine.getUrl()).isEqualTo(testUrl);
        assertThat(queries.get(testKey)).isEqualTo(testValue);
    }
}
