package webserver.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestHeader {
    private final Map<String, String> headerMap;
    private final Map<String, String> cookie;

    private HttpRequestHeader(Map<String, String> headerMap, Map<String, String> cookie) {
        this.headerMap = headerMap;
        this.cookie = cookie;
    }

    public static HttpRequestHeader makeHttpRequestHeader(List<String> headerLine) {
        Map<String, String> headerMap = new HashMap<>();
        Map<String, String> cookieMap = new HashMap<>();

        for(String line: headerLine){
            String[] lineSplit = line.split(":");

            if(lineSplit.length == 2) {
                if ("Cookie".equals(lineSplit[0].trim()))
                    cookieMap = makeCookieMap(lineSplit[1].trim());

                headerMap.put(lineSplit[0].trim(), lineSplit[1].trim());
            }
        }

        return new HttpRequestHeader(headerMap, cookieMap);
    }

    public static Map<String, String> makeCookieMap(String cookieString){
        Map<String, String> cookieMap = new HashMap<>();
        String[] lineSplit = cookieString.split(";");

        for(String line: lineSplit){
            String[] query = line.split("=");

            if(query.length == 2){
                cookieMap.put(query[0].trim(), query[1].trim());
            }
        }

        return cookieMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public Map<String, String> getCookie() {
        return cookie;
    }
}
