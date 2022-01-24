package http;

import util.HttpRequestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestParams {

    private final Map<String,String> params;

    public RequestParams(){
        this.params=new HashMap<>();
    }

    public void addRequestParams(String queryString){
        params.putAll(HttpRequestUtils.parseQueryString(queryString));
    }

    public Map<String,String> getParams(){
        return Collections.unmodifiableMap(params);
    }
}
