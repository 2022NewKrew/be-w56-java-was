package webserver.http.request;

import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestHeaders {
    private final Map<String, String> headers = new HashMap<>();

    public RequestHeaders(){}

    public void add(HttpRequestUtils.Pair pair){
        if(pair != null){
            headers.put(pair.getKey(), pair.getValue());
        }
    }

    public Map<String, String> getHeaders(){
        return headers;
    }
}
