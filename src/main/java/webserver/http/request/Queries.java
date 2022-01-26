package webserver.http.request;

import java.util.Map;

public class Queries {
    private final Map<String, String> queries;

    public Queries(Map<String, String> queries){
        this.queries = queries;
    }

    public Map<String, String> getQueries(){
        return queries;
    }
}
