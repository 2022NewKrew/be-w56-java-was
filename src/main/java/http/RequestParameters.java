package http;

import java.util.Map;

public class RequestParameters {

    private final Map<String, String> parameters;

    public static RequestParameters of(Map<String, String> parameters) {
        return new RequestParameters(parameters);
    }

    private RequestParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String get(String key) {
        return parameters.getOrDefault(key, "");
    }
}
