package http;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class QueryString {
    public static final String QUERY_STRING_KEY_VALUE_SPLIT_DELIMITER = "=";
    public static final String QUERY_STRING_PAIR_SPLIT_DELIMITER = "&";

    private Map<String, String> queryStringMap;

    public QueryString(String pathAndQueryString) {
        makeQueryStringMap(pathAndQueryString);
    }

    private void makeQueryStringMap(String queryString) {
        String[] splitQueryString = queryString.split(QUERY_STRING_PAIR_SPLIT_DELIMITER);
        queryStringMap = new HashMap<>();
        for (String pair : splitQueryString) {
            String[] split = pair.split(QUERY_STRING_KEY_VALUE_SPLIT_DELIMITER);
            queryStringMap.put(URLDecoder.decode(split[0], StandardCharsets.UTF_8), URLDecoder.decode(split[1], StandardCharsets.UTF_8));
        }
    }

    public Map<String, String> getQueryStringMap() {
        return queryStringMap;
    }
}
