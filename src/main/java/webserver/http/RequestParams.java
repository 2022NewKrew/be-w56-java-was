package webserver.http;

import java.util.Map;

public class RequestParams {

    private Map<String, String> requestParams;

    RequestParams(Map<String, String> params) {
        this.requestParams = params;
    }

    public String getParam(String key) {
        return requestParams.get(key);
    }

}
