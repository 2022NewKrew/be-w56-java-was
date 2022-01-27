package http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestHeaders {
    private static final Logger log = LoggerFactory.getLogger(RequestHeaders.class);

    private final Map<String, String> headers = new HashMap<>();

    public RequestHeaders(){}

    public void add(HttpRequestUtils.Pair pair){
        if(pair != null){
            headers.put(pair.getKey(), pair.getValue());
            if(pair.getKey().equals("Cookie")){
                log.info(pair.getKey() + " : " + pair.getValue());
            }
        }
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

    public String get(String key){
        return headers.get(key);
    }
}
