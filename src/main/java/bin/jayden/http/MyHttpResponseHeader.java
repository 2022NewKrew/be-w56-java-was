package bin.jayden.http;


import bin.jayden.util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyHttpResponseHeader {
    private final Map<String, String> header = new HashMap<>();

    public void addHeader(HttpRequestUtils.Pair pair) {
        header.put(pair.getKey(), pair.getValue());
    }

    public Set<Map.Entry<String, String>> getEntrySet() {
        return header.entrySet();
    }

    public String getHeaderValue(String key) {
        return header.get(key);
    }
}
