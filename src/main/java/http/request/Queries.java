package http.request;

import util.PasswordEncoder;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Queries {
    private final Map<String, String> queries;

    public Queries(Map<String, String> queries){
        this.queries = queries;
    }

    public Map<String, String> getQueries(){
        return queries;
    }

    public String get(String key){
        return queries.get(key);
    }

    public void encode(String key){
        if(queries.get(key) != null){
            queries.put(key, PasswordEncoder.encrypt(queries.get(key)));
        }
    }
}
