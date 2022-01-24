package webserver;

import java.util.HashMap;
import java.util.Optional;

public class RequestMap {
    private final HashMap<String, Object> headers = new HashMap<>();

    public Optional<Object> getHeader(String headerName){
        return Optional.ofNullable(headers.get(headerName));
    }

    public void addHeader(String headerName, Object object){
        headers.put(headerName, object);
    }
}
