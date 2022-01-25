package webserver.core.http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Header implements Iterable<String>{

    private final Map<String, String> attributes = new HashMap<>();

    public void addHeaderValue(String key, String value){
        attributes.put(key, value);
    }

    public String getHeaderValue(String key){
        return attributes.get(key);
    }

    @Override
    public Iterator<String> iterator() {
        return attributes.keySet().iterator();
    }
}
