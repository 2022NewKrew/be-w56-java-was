package webserver.controller.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.Maps;

public class RequestHeader {
    private Map<String, String> header = Maps.newHashMap();

    public static RequestHeader from(BufferedReader br) throws IOException {
        RequestHeader requestHeader = new RequestHeader();
        String line;
        while (!("".equals(line = br.readLine()))) {
            String[] token = line.split(": ");
            requestHeader.put(token[0], token[1]);
        }
        return requestHeader;
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
