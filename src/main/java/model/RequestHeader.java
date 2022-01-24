package model;

import java.util.HashMap;
import java.util.Map;

public class RequestHeader {
    private final Map<String, String> header;

    public RequestHeader() {
        header = new HashMap<>();
    }

    public String getAccept(){
        return header.get("Accept").split(",")[0];
    }

    public String getHeader(String key) {
        return header.get(key);
    }

    public void putHeader(String key, String value) {
        header.put(key, value);
    }
}
