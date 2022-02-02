package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.Maps;

import http.request.parser.HttpRequestUtils;


public class RequestHeader {
    private Map<String, String> header = Maps.newHashMap();

    public static RequestHeader from(BufferedReader br) throws IOException {
        return HttpRequestUtils.parseWholeHeader(br);
    }

    public void put(String key, String value) {
        header.put(key, value);
    }

    public String get(String key) {
        if (!header.containsKey(key)) {
            return null;
        }
        return header.get(key);
    }

}
