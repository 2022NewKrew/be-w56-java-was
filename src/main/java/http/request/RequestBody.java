package http.request;

import exception.BadRequestException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestBody {

    private final Map<String, String> bodyData;

    public RequestBody(String rawBody) {
        this.bodyData = new HashMap<>();
        if (rawBody == null) {
            return;
        }
        setBodyData(rawBody);
    }

    private void setBodyData(String rawBody) {
        List<String> dataLine = List.of(rawBody.split("&"));
        List<String> components;
        String key;
        String value;
        try {
            for (String line : dataLine) {
                components = List.of(line.split("="));
                key = URLDecoder.decode(components.get(0), StandardCharsets.UTF_8);
                value = URLDecoder.decode(components.get(1), StandardCharsets.UTF_8);
                bodyData.put(key, value);
            }
        } catch (Exception exception) {
            throw new BadRequestException("body decoding에 실패하였습니다");
        }
    }

    public Map<String, String> getBodyData() {
        return bodyData;
    }
}
