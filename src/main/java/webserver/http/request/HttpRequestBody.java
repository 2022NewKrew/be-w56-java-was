package webserver.http.request;

import java.util.Map;
import java.util.Optional;

public class HttpRequestBody {
    private final Map<String, String> query;

//    public HttpRequestBody(Optional<Map<String, String>> query) {
//        this.query = query;
//    }
//
//    public Optional<Map<String, String>> getQuery() {
//        return query;
//    }

        public HttpRequestBody(Map<String, String> query) {
        this.query = query;
    }

    public Map<String, String> getQuery() {
        return query;
    }
}
