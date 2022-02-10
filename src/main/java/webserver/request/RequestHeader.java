package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestHeader {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String EMPTY = "";
    private static final String DOUBLE_DOT = ":";

    private final Map<String, String> headers;

    private RequestHeader(Map<String, String> headers) {
        this.headers = Collections.unmodifiableMap(headers);
    }

    public static RequestHeader from(BufferedReader br) throws IOException {
        Map<String, String> map = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null) {
            if (line.equals(EMPTY)) {
                break;
            }
            String key = line.split(DOUBLE_DOT)[0];
            String value = line.split(DOUBLE_DOT)[1].trim();
            map.put(key, value);
        }
        return new RequestHeader(map);
    }

    public String getContentType() {
        return this.headers.containsKey(CONTENT_TYPE) ? this.headers.get(CONTENT_TYPE) : null;
    }

    public Integer getContentLength() {
        return this.headers.containsKey(CONTENT_LENGTH) ? Integer.valueOf(this.headers.get(CONTENT_LENGTH)) : 0;
    }

    public String getHeaderByKey(String key) {
        return this.headers.containsKey(key) ? this.headers.get(key) : null;
    }

    @Override
    public String toString() {
        return "RequestHeader{" +
                "headers=" + headers +
                '}';
    }
}
