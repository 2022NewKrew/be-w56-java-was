package webserver.controller.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import util.HttpRequestUtils;
import util.IOUtils;

public class RequestBody {

    private Map<String, String> requestBody;

    private RequestBody() { }

    public static RequestBody of () {
        RequestBody requestBody = new RequestBody();
        requestBody.requestBody = Maps.newHashMap();
        return requestBody;
    }
    public static RequestBody of (BufferedReader br, int length) throws IOException {
        RequestBody requestBody = new RequestBody();
        
        String bodyString = IOUtils.readData(br, length);
        requestBody.requestBody = HttpRequestUtils.parseQueryString(bodyString);

        return requestBody;
    }

    public String get(String key) {
        if (!requestBody.containsKey(key)){
            return null;
        }
        return requestBody.get(key);
    }
}
