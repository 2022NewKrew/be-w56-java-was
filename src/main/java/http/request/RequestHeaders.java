package http.request;

import http.util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestHeaders {

    private Map<String, String> headers = new HashMap<>();

    private void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getHeader(String key) {
        return headers.getOrDefault(key, "");
    }

    public static RequestHeaders createRequestHeader(BufferedReader br) throws IOException {
        RequestHeaders requestHeader = new RequestHeaders();
        String line = null;
        while((line = br.readLine()) != null && !("".equals(line))) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            requestHeader.setHeader(pair.getKey(), pair.getValue());
        }
        return requestHeader;
    }
}
