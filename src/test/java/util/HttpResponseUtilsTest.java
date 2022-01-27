package util;

import controller.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by melodist
 * Date: 2022-01-27 027
 * Time: 오후 5:26
 */
class HttpResponseUtilsTest {

    @Test
    public void createResponseString() {
        // given
        Response response = new Response.Builder().ok()
                .build();

        // when
        String responseString = HttpResponseUtils.createResponseString(response);

        // then
        Assertions.assertThat(responseString).isEqualTo("HTTP/1.1 200 OK\r\n");
    }

    @Test
    public void createResponseStringWithHeader() {
        // given
        Map<String, String> headers = Map.of("test-key", "test-value");
        Response response = new Response.Builder().ok()
                .headers(headers)
                .build();

        // when
        String responseString = HttpResponseUtils.createResponseString(response);

        // then
        Assertions.assertThat(responseString).isEqualTo("HTTP/1.1 200 OK\r\n" + "test-key: test-value\r\n\r\n");
    }



}
