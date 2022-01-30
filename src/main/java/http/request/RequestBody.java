package http.request;

import exception.BadRequestException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestBody {

    private final Map<String, String> bodyData;

    public RequestBody(Map<String, String> bodyData) {
        this.bodyData = bodyData;
    }

    public static RequestBody stringToRequestBody(String body) {
        if (body.isEmpty()) {
            return new RequestBody(new HashMap<>());
        }
        List<String> bodyLines = List.of(body.split("&"));
        return new RequestBody(getBodyData(bodyLines));
    }

    private static Map<String, String> getBodyData(List<String> bodyLines) {
        Map<String, String> result = new HashMap<>();
        List<String> components;
        String key;
        String value;
        try {
            for (String line : bodyLines) {
                components = List.of(line.split("="));
                key = URLDecoder.decode(components.get(0), StandardCharsets.UTF_8);
                value = URLDecoder.decode(components.get(1), StandardCharsets.UTF_8);
                result.put(key, value);
            }
            return result;
        } catch (Exception exception) {
            throw new BadRequestException("body decoding에 실패하였습니다");
        }
    }

    public Map<String, String> getBodyData() {
        return bodyData;
    }
}
