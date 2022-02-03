package webserver.web.request;

import lombok.Getter;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Body {

    private final Map<String, String> body = new HashMap<>();

    public Body() {
    }

    public void putUrlEncodedBody(String data) {
        String[] dataSet = data.split("&");
        Arrays.stream(dataSet).forEach(set -> {
            body.put(set.split("=")[0], URLDecoder.decode(set.split("=")[1], StandardCharsets.UTF_8));
        });
    }

    public String getData(String target) {
        return body.get(target);
    }
}
