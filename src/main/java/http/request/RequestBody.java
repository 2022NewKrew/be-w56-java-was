package http.request;

import exception.BadRequestException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestBody {

    private final Map<String, String> bodyData;

    public RequestBody(Map<String, String> bodyData) {
        this.bodyData = Collections.unmodifiableMap(bodyData);
    }

    public static RequestBody stringToRequestBody(String body) {
        if (body.isEmpty()) {
            return new RequestBody(Collections.unmodifiableMap(new HashMap<>()));
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

                checkComponents(components);

                key = URLDecoder.decode(components.get(0), StandardCharsets.UTF_8);
                value = URLDecoder.decode(components.get(1), StandardCharsets.UTF_8);
                result.put(key, value);
            }
            return Collections.unmodifiableMap(result);
        } catch (Exception exception) {
            throw new BadRequestException("body decoding에 실패하였습니다");
        }
    }

    private static void checkComponents(List<String> components) {
        if (components.size() != 2 || components.contains("")) {
            throw new BadRequestException("부적절한 body 데이터가 존재합니다.");
        }
    }

    public Map<String, String> getBodyData() {
        return bodyData;
    }
}
