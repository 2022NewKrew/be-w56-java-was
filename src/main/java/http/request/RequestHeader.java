package http.request;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Constant;

public class RequestHeader {

    private final Map<String, String> components;

    public RequestHeader(Map<String, String> components) {
        this.components = Collections.unmodifiableMap(components);
    }

    public static RequestHeader stringToRequestHeader(String headers) {
        if(headers.isEmpty()) {
            return new RequestHeader(Collections.unmodifiableMap(new HashMap<>()));
        }
        List<String> headerLines = List.of(headers.split(Constant.lineBreak));
        return new RequestHeader(getComponents(headerLines));
    }

    private static Map<String, String> getComponents(List<String> headerLines) {
        Map<String, String> result = new HashMap<>();
        List<String> splitLine;
        for (String headerLine : headerLines) {
            splitLine = List.of(headerLine.split(": "));
            result.put(splitLine.get(0), splitLine.get(1));
        }
        return Collections.unmodifiableMap(result);
    }

    public boolean has(String key) {
        return components.containsKey(key);
    }

    public String get(String key) {
        return components.get(key);
    }
}
