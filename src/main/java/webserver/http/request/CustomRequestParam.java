package webserver.http.request;

import java.util.HashMap;
import java.util.Map;

public class CustomRequestParam {
    private Map<String, String> requestParam = new HashMap<>();

    public void addRequestParam(Map<String, String> requestParam) {
        for (String s : requestParam.keySet()) {
            this.requestParam.put(s, requestParam.get(s));
        }
    }

    public Map<String, String> getRequestParam() {
        return requestParam;
    }
}
