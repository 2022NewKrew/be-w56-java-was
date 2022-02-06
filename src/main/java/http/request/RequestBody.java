package http.request;

import java.util.Map;

public class RequestBody {
    private final Map<String, String> parsedBody;

    RequestBody(Map<String, String> map) {
        this.parsedBody = map;
    }

    public String getBody(String name) {
        return parsedBody.get(name);
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "parsedBody=" + parsedBody +
                '}';
    }
}
