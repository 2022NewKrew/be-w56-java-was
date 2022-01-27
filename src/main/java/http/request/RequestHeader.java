package http.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Constant;

public class RequestHeader {

    private final Map<String, String> components;

    public RequestHeader(Map<String, String> components) {
        this.components = components;
    }

    public RequestHeader(String headerString) {
        this(new HashMap<>());

        setComponents(List.of(headerString.split(Constant.lineBreak)));
    }

    private void setComponents(List<String> headerLines) {
        List<String> splitLine;
        for (String headerLine : headerLines) {
            splitLine = List.of(headerLine.split(": "));
            this.components.put(splitLine.get(0), splitLine.get(1));
        }
    }

    public boolean has(String key) {
        return components.containsKey(key);
    }

    public String get(String key) {
        return components.get(key);
    }
}
