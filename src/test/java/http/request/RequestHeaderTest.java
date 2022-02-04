package http.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("RequestHeader 테스트")
class RequestHeaderTest {

    @DisplayName("stringToRequestHeader 메서드는 올바른 headerString 을 입력받았을 때 RequestHeader 를 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "test1: this is test1",
            "test2: this is test2\r\ntest1: this is test1",
            "test3: this is test3\r\ntest2: this is test2\r\ntest1: this is test1\r\n"
    })
    void stringToRequestHeader(String testHeaderString) {
        //give
        List<String> components = List.of(testHeaderString.split("\r\n"));
        String testItem = components.get(0);
        //when
        RequestHeader header = RequestHeader.stringToRequestHeader(testHeaderString);
        //then 1: String is empty
        if (testItem.isEmpty()) {
            assertThatCode(() -> RequestHeader.stringToRequestHeader(
                    testHeaderString)).doesNotThrowAnyException();
            return;
        }
        //then 2: String is not empty
        String testKey = testItem.split(": ")[0];
        String testValue = testItem.split(": ")[1];
        assertThat(header.getComponent(testKey)).isEqualTo(testValue);
    }

    @DisplayName("stringToRequestHeader 메서드는 올바른 headerString 을 입력받았을 때 RequestHeader 를 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "test1: this is test1\r\nCookie: a=b",
            "test2: this is test2\r\ntest1: this is test1\r\nCookie: b=c; a=b",
            "test3: this is test3\r\ntest2: this is test2\r\ntest1: this is test1\r\nCookie: c=d; b=c; a=b"
    })
    void stringToRequestHeaderWithCookie(String testHeaderString) {
        //give
        List<String> components = List.of(testHeaderString.split("\r\n"));
        String testItem = components.get(0);
        String testCookie = components.get(components.size() - 1);
        //when
        RequestHeader header = RequestHeader.stringToRequestHeader(testHeaderString);
        //then 1: String is empty
        if (testItem.isEmpty()) {
            assertThatCode(() -> RequestHeader.stringToRequestHeader(
                    testHeaderString)).doesNotThrowAnyException();
            return;
        }
        //then 2: String is not empty
        String testKey = testItem.split(": ")[0];
        String testValue = testItem.split(": ")[1];
        String testCookieKey = testCookie.split(": ")[1].trim().split(";")[0].split("=")[0];
        String testCookieValue = testCookie.split(": ")[1].trim().split(";")[0].split("=")[1];
        assertThat(header.getComponent(testKey)).isEqualTo(testValue);
        assertThat(header.getCookie(testCookieKey)).isEqualTo(testCookieValue);
    }

    @DisplayName("hasComponent 메서드는 RequestHeader 가 가지고 있는 key 를 입력했을 때 true 를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"key1", "key2"})
    void hasComponent(String testKey) {
        //give
        Map<String, String> components = Map.of("key1", "value1", "key2", "value2");
        RequestHeader header = new RequestHeader(components, new HashMap<>());
        //when
        boolean result = header.hasComponent(testKey);
        //then
        assertThat(result).isTrue();
    }

    @DisplayName("hasComponent 메서드는 RequestHeader 가 가지고 있지 않은 key 를 입력했을 때 false 를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "hasNotKey1", "hasNotKey2"})
    void doesNotHaveComponent(String testKey) {
        //give
        Map<String, String> components = Map.of("key1", "value1", "key2", "value2");
        RequestHeader header = new RequestHeader(components, new HashMap<>());
        //when
        boolean result = header.hasComponent(testKey);
        //then
        assertThat(result).isFalse();
    }

    @DisplayName("getComponent 메서드는 RequestHeader 가 가지고 있는 key 를 입력했을 때 value 를 반환한다.")
    @ParameterizedTest
    @CsvSource({"key1,value1", "key2,value2"})
    void getComponent(String testKey, String testValue) {
        //give
        Map<String, String> components = Map.of("key1", "value1", "key2", "value2");
        RequestHeader header = new RequestHeader(components, new HashMap<>());
        //when
        String value = header.getComponent(testKey);
        //then
        assertThat(value).isEqualTo(testValue);
    }

    @DisplayName("hasCookie 메서드는 RequestHeader 가 가지고 있는 key 를 입력했을 때 true 를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"key1", "key2"})
    void hasCookie(String testKey) {
        //give
        Map<String, String> cookie = Map.of("key1", "value1", "key2", "value2");
        RequestHeader header = new RequestHeader(new HashMap<>(), cookie);
        //when
        boolean result = header.hasCookie(testKey);
        //then
        assertThat(result).isTrue();
    }

    @DisplayName("hasCookie 메서드는 RequestHeader 가 가지고 있지 않은 key 를 입력했을 때 false 를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "hasNotKey1", "hasNotKey2"})
    void doesNotHaveCookie(String testKey) {
        //give
        Map<String, String> cookie = Map.of("key1", "value1", "key2", "value2");
        RequestHeader header = new RequestHeader(new HashMap<>(), cookie);
        //when
        boolean result = header.hasCookie(testKey);
        //then
        assertThat(result).isFalse();
    }

    @DisplayName("getCookie 메서드는 RequestHeader 가 가지고 있는 key 를 입력했을 때 true 를 반환한다.")
    @ParameterizedTest
    @CsvSource({"key1,value1", "key2,value2"})
    void getCookie(String testKey, String testValue) {
        //give
        Map<String, String> cookie = Map.of("key1", "value1", "key2", "value2");
        RequestHeader header = new RequestHeader(new HashMap<>(), cookie);
        //when
        String value = header.getCookie(testKey);
        //then
        assertThat(value).isEqualTo(testValue);
    }
}
