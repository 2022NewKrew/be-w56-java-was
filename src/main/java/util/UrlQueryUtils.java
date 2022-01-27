package util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UrlQueryUtils {
    public static Map<String, String> parseUrlQuery(String url){
        Map<String, String> urlQuery = new HashMap<>();

        String data = URLDecoder.decode(url, StandardCharsets.UTF_8);
        for(String query: data.split("&")){
            urlQuery.put(query.split("=")[0], query.split("=")[1]);
        }

        return urlQuery;
    }
}
