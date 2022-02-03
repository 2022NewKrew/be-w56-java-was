package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import http.request.parser.HttpRequestUtils;


public class RequestHeader {
    private Map<String, List<String>> header = Maps.newHashMap();

    public static RequestHeader from(BufferedReader br) throws IOException {
        return HttpRequestUtils.parseWholeHeader(br);
    }

    public void put(String key, String value) {
        if (!header.containsKey(key)) {
            header.put(key, new ArrayList<>());
        }
        List<String> list = header.get(key);
        list.addAll(Arrays.asList(value.split(",")));
    }

    public String getFirst(String key) {
        if (!header.containsKey(key)) {
            return null;
        }
        List<String> list = header.get(key);
        return list.get(0);
    }

    public List<String> getAll(String key) {
        if (!header.containsKey(key)) {
            return null;
        }
        return header.get(key);
    }

}
