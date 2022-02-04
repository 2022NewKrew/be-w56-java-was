package framework.http.request;

import framework.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestBody {
    private static final String REQUEST_BODY_KEY_VALUE_SPLIT_DELIMITER = "=";
    private static final String REQUEST_BODY_PAIR_SPLIT_DELIMITER = "&";

    private Map<String, String> requestBodyMap;

    public RequestBody(BufferedReader bufferedReader, int contentLength) throws IOException {
        makeRequestBodyMap(bufferedReader, contentLength);
    }

    private void makeRequestBodyMap(BufferedReader bufferedReader, int contentLength) throws IOException {
        String requestBodyString = IOUtils.readData(bufferedReader, contentLength);
        String[] splitRequestBodyString = requestBodyString.split(REQUEST_BODY_PAIR_SPLIT_DELIMITER);
        Map<String, String> requestBodyMap = new HashMap<>();
        for (String pair : splitRequestBodyString) {
            String[] split = pair.split(REQUEST_BODY_KEY_VALUE_SPLIT_DELIMITER);
            requestBodyMap.put(URLDecoder.decode(split[0], StandardCharsets.UTF_8), URLDecoder.decode(split[1], StandardCharsets.UTF_8));
        }

        this.requestBodyMap = Collections.unmodifiableMap(requestBodyMap);
    }

    public String getValue(String key) {
        return requestBodyMap.get(key);
    }

    public Map<String, String> getRequestBodyMap() {
        return requestBodyMap;
    }
}
