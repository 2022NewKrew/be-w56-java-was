package http.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        assertThat(header.get(testKey)).isEqualTo(testValue);
    }

    @DisplayName("has 메서드는 RequestHeader 가 가지고 있는 key 를 입력했을 때 true 를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"key1", "key2"})
    void has(String testKey) {
        //give
        Map<String, String> components = Map.of("key1", "value1", "key2", "value2");
        RequestHeader header = new RequestHeader(components);
        //when
        boolean result = header.has(testKey);
        //then
        assertThat(result).isTrue();
    }

    @DisplayName("has 메서드는 RequestHeader 가 가지고 있지 않은 key 를 입력했을 때 false 를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "hasNotKey1", "hasNotKey2"})
    void hasNot(String testKey) {
        //give
        Map<String, String> components = Map.of("key1", "value1", "key2", "value2");
        RequestHeader header = new RequestHeader(components);
        //when
        boolean result = header.has(testKey);
        //then
        assertThat(result).isFalse();
    }

    @DisplayName("get 메서드는 RequestHeader 가 가지고 있는 key 를 입력했을 때 value 를 반환한다.")
    @ParameterizedTest
    @CsvSource({"key1,value1", "key2,value2"})
    void get(String testKey, String testValue) {
        //give
        Map<String, String> components = Map.of("key1", "value1", "key2", "value2");
        RequestHeader header = new RequestHeader(components);
        //when
        String value = header.get(testKey);
        //then
        assertThat(value).isEqualTo(testValue);
    }
}
