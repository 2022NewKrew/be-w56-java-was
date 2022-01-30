package http;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseBodyTest {

    @Test
    void createResponseHeader() {
        ResponseBody responseBody = new ResponseBody(new byte[]{1, 1, 1, 1});

        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "text/html;charset=utf-8");
        map.put("Content-Length", "4");
        Headers headers = Headers.create(map);

        assertThat(responseBody.createResponseHeader()).isEqualTo(headers);
    }
}
