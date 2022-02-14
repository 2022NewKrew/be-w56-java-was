package webserver.http.request.header;

import com.google.common.collect.Maps;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class ParameterMap {
    private static final int VALUE_INDEX = 1;
    private final Map<String, String> map;

    public ParameterMap(String fullPath) {
        String[] urlSplitList = fullPath.split("\\?");
        Map<String, String> parseMap = Maps.newHashMap();

        if (urlSplitList.length > VALUE_INDEX)
            parseMap = HttpRequestUtils.parseQueryString(urlSplitList[VALUE_INDEX], "&", "=");

        map = Map.copyOf(new HashMap<>(parseMap));
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public String get(String key) {
        return map.get(key);
    }

    public Map<String, String> getInstance() {
        return Map.copyOf(map);
    }
}
