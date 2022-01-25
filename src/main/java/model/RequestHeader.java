package model;

import java.util.HashMap;
import java.util.Map;

public class RequestHeader {
    private final Map<String, String> header;
    private final Map<String, String> parameter;

    public RequestHeader() {
        header = new HashMap<>();
        parameter = new HashMap<>();
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

    public String getParameter(String key) {
        return parameter.get(key);
    }

    public void putParameter(String key, String value) {
        parameter.put(key, value);
    }
}
