package http.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("RequestBody 테스트")
class RequestBodyTest {

    @DisplayName("stringToRequestBody 메서드는 올바른 bodyString 을 입력받았을 때 RequestBody 를 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "test=test",
            "test=test&isOk=isOk",
            "test=test&is=is&really=really&ok=ok"
    })
    void stringToRequestBody(String testBodyString) {
        //give
        List<String> splitItem = List.of(testBodyString.split("&"));
        //when
        RequestBody body = RequestBody.stringToRequestBody(testBodyString);
        //then 1 : 빈 String인 경우
        if(testBodyString.isEmpty()){
            assertThat(body.getBodyData().size()).isEqualTo(0);
            return;
        }
        //then 2 : 빈 String이 아닌 경우
        String testKey = splitItem.get(0).split("=")[0];
        String testValue = splitItem.get(0).split("=")[1];

        assertThat(body.getBodyData().size()).isEqualTo(splitItem.size());
        assertThat(body.getBodyData().get(testKey)).isEqualTo(testValue);
    }
}
