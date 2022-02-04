package http.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ResponseHeader 테스트")
class ResponseHeaderTest {

    @DisplayName("addContentType 메서드에 파라미터로 url 을 넘겼을 때 해당 올바른 ContentType 을 추가한다.")
    @ParameterizedTest
    @ValueSource(strings = {"test.html", "test.css", "test.js"})
    void addContentTypeToUrl(String testUrl) {
        //give
        ResponseHeader header = new ResponseHeader();
        String testExtension = testUrl.split("\\.")[1];
        //when
        header.addContentType(testUrl);
        //then
        assertThat(header.getComponentString()).contains("Content-Type", testExtension);
    }

    @DisplayName("addContentType 메서드에 파라미터로 url 을 넘겼을 때 해당 올바른 ContentType 을 추가한다.")
    @ParameterizedTest
    @EnumSource(ContentType.class)
    void testAddContentToContentType(ContentType testType) {
        //give
        ResponseHeader header = new ResponseHeader();
        String testExtension = testType.getExtension();
        //when
        header.addContentType(testType);
        //then
        assertThat(header.getComponentString()).contains("Content-Type", testExtension);
    }

    @DisplayName("addContentLength 파라미터로 ContentLength 를 넘겼을 때 올바른 ContentLength 를 추가한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 10, 100})
    void addContentLength(int testLength) {
        //give
        ResponseHeader header = new ResponseHeader();
        //when
        header.addContentLength(testLength);
        //then
        assertThat(header.getComponentString()).contains("Content-Length",
                String.valueOf(testLength));
    }

    @DisplayName("addLocation 파라미터로 location 을 넘겼을 때 올바른 location 을 추가한다.")
    @ParameterizedTest
    @ValueSource(strings = {"/testPath1", "/testPath2", "/testPath3"})
    void addLocation(String testLocation) {
        //give
        ResponseHeader header = new ResponseHeader();
        //when
        header.addLocation(testLocation);
        //then
        assertThat(header.getComponentString()).contains("Location", testLocation);
    }

    @DisplayName("addCookie 파라미터로 cookie 값을 넘겼을 때 올바른 cookie 값을 추가한다.")
    @Test
    void addCookie() {
        //give
        Map<String, String> testCookie = Map.of("testCookie1", "firstCookie", "testCookie2",
                "secondCookie");
        ResponseHeader header = new ResponseHeader();
        //when
        header.addCookie(testCookie);
        //then
        assertThat(header.getComponentString()).contains("Set-Cookie", "testCookie1", "testCookie2",
                "firstCookie", "secondCookie");
    }

    @DisplayName("getComponentString 메서드를 실행하면 header 가 가지고 있는 모든 요소를 ResponseHeader 에 맞게 String 으로 변환한다.")
    @Test
    void getComponentString() {
        //give
        Map<String, String> testComponents = Map.of("testComponent", "firstComponent");
        Map<String, String> testCookie = Map.of("testCookie", "firstCookie");
        ResponseHeader header = new ResponseHeader(testComponents, testCookie);
        //when
        String result = header.getComponentString();
        //then
        assertThat(result).contains("testComponent", "firstComponent", "Set-Cookie", "testCookie",
                "firstCookie");
    }
}
