package webserver.controller.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import util.HttpRequestUtils;

public class RequestBody {

    private Map<String, String> requestBody;

    private RequestBody() { }

    public static RequestBody of () throws IOException {
        RequestBody requestBody = new RequestBody();
        requestBody.requestBody = Maps.newHashMap();
        return requestBody;
    }
    public static RequestBody of (BufferedReader br) throws IOException {
        RequestBody requestBody = new RequestBody();
        requestBody.requestBody = HttpRequestUtils.parseBody(br);
        return requestBody;
    }

    public String get(String key) {
        if (!requestBody.containsKey(key)){
            return null;
        }
        return requestBody.get(key);
    }
}
