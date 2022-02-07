package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {
    private Map<String, Object> attribute;

    public HttpSession(){
        this.attribute = new HashMap<>();
    }

    public Object getValue(String key){
        return attribute.get(key);
    }
}
