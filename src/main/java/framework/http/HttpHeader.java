package framework.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpHeader {
    private static final Logger log = LoggerFactory.getLogger(HttpHeader.class);
    private static final String HEADER_KEY_VALUE_SPLIT_DELIMITER = ": ";

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String LOCATION = "Location";

    private Map<String, String> headers;

    public HttpHeader() {
        headers = new HashMap<>();
    }

    public HttpHeader(BufferedReader bufferedReader) throws IOException {
        makeHeaders(bufferedReader);
    }

    private void makeHeaders(BufferedReader bufferedReader) throws IOException {
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

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setContentType(MediaType contentType) {
        headers.put(CONTENT_TYPE, contentType.getText());
    }

    public void setContentLength(int contentLength) {
        headers.put(CONTENT_LENGTH, String.valueOf(contentLength));
    }

    public void setLocation(String location) {
        headers.put(LOCATION, location);
    }

    public String getValue(String key) {
        return headers.get(key);
    }

    public String getContentType() {
        return headers.get(CONTENT_TYPE);
    }

    public String getContentLength() {
        return headers.get(CONTENT_LENGTH);
    }

    public String getLocation() {
        return headers.get(LOCATION);
    }
}
