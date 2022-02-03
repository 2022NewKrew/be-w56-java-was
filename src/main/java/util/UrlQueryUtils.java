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
            String[] querySplit = query.split("=");

            if(querySplit.length == 2)
                urlQuery.put(querySplit[0], querySplit[1]);
        }

        return urlQuery;
    }
}
