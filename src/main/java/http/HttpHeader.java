package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpHeader {
    private static final Logger log = LoggerFactory.getLogger(HttpHeader.class);
    public static final String HEADER_KEY_VALUE_SPLIT_DELIMITER = ": ";

    private Map<String, String> headers;

    public HttpHeader() {
        headers = new HashMap<>();
    }

    public HttpHeader(BufferedReader bufferedReader) throws IOException {
        makeHeaders(bufferedReader);
    }

    public HttpHeader(String contentType, int lengthOfBodyContent) {
        headers = new HashMap<>();
        headers.put("Content-Type", contentType);
        headers.put("Content-Length", String.valueOf(lengthOfBodyContent));
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void makeHeaders(BufferedReader bufferedReader) throws IOException {
        headers = new HashMap<>();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            log.debug("header: {}", line);
            if (line.equals("")) {
                break;
            }

            String[] split = line.split(HEADER_KEY_VALUE_SPLIT_DELIMITER);
            String key = split[0];
            String value = split[1];
            headers.put(key, value);
        }
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getValue(String key) {
        return headers.get(key);
    }
}
