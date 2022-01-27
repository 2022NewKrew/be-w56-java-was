package http.request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestBody {

    Logger logger = LoggerFactory.getLogger(RequestBody.class);

    private final Map<String, String> bodyData;

    public RequestBody(String rawBody) {
        this.bodyData = new HashMap<>();
        if (rawBody == null) {
            return;
        }
        setBodyData(rawBody);
        logger.info(bodyData.toString());
    }

    private void setBodyData(String rawBody) {
        List<String> dataLine = List.of(rawBody.split("&"));
        List<String> components;
        String key;
        String value;
        try {
            for (String line : dataLine) {
                components = List.of(line.split("="));
                key = URLDecoder.decode(components.get(0), "utf-8");
                value = URLDecoder.decode(components.get(1), "utf-8");
                bodyData.put(key, value);
            }
        } catch (UnsupportedEncodingException exception) {
            throw new IllegalArgumentException("body decoding에 실패하였습니다");
        }
    }
}
