package webserver.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestHeader {
    private final Map<String, String> headerMap;

    private HttpRequestHeader(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public static HttpRequestHeader makeHttpRequestHeader(List<String> headerLine) {
        Map<String, String> headerMap = new HashMap<>();

        for(String line: headerLine){
            String[] lineSplit = line.split(":");

            if(lineSplit.length == 2)
                headerMap.put(lineSplit[0].trim(), lineSplit[1].trim());
        }

        return new HttpRequestHeader(headerMap);
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}
