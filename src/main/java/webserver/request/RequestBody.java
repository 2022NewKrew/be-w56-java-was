package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import util.HttpRequestUtils;

public class RequestBody {

    private Map<String, String> requestBody;

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
