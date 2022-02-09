package webserver.request;

import java.util.HashMap;
import java.util.Map;

import static webserver.request.RequestUri.PARAM_SEPARATOR;
import static webserver.request.RequestUri.QUERY_SEPARATOR;

public class RequestBody {
    private final String body;
    private final Map<String, String> parsedBody = new HashMap<>();

    private RequestBody(String body) {
        this.body = body;
    }

    public static RequestBody from(String body) {
        return new RequestBody(body);
    }

    public String getBody() {
        return body;
    }

    public RequestBody parseBody() {
        for (String s : body.split(QUERY_SEPARATOR)) {
            String[] split = s.split(PARAM_SEPARATOR);
            if(split.length >= 2) {
                parsedBody.put(split[0], split[1]);
            }
        }
        return this;
    }

    public String getBody(String key) { return parsedBody.containsKey(key) ? parsedBody.get(key) : null;}

    @Override
    public String toString() {
        return "RequestBody{" +
                "body='" + body + '\'' +
                '}';
    }
}
