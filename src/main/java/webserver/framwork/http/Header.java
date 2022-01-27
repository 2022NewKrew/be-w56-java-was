package webserver.framwork.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Header implements Iterable<String>{

    private final Map<String, String> attributes = new HashMap<>();

    public void addValue(String key, String value){
        attributes.put(key, value);
    }

    public String getValue(String key){
        return attributes.get(key);
    }

    @Override
    public Iterator<String> iterator() {
        return attributes.keySet().iterator();
    }
}
