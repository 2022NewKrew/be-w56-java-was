package http.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Constant;
import webserver.HttpMethod;

public class RequestHeader {

    private HttpMethod method;
    private String url;
    private final Map<String, String> query = new HashMap<>();
    private String protocol;
    private final Map<String, String> components = new HashMap<>();

    public RequestHeader(String rawHeader) {
        List<String> headerLines = parsingRawHeader(rawHeader);
        String firstLine = headerLines.get(0);
        List<String> otherLine = headerLines.subList(1, headerLines.size());
        setFirstLineComponents(firstLine);
        setComponents(otherLine);
    }

    private List<String> parsingRawHeader(String rawHeader) {
        return List.of(rawHeader.split(Constant.lineBreak));
    }

    private void setFirstLineComponents(String firstLine) {
        List<String> components = List.of(firstLine.split(" "));
        this.method = HttpMethod.valueOf(components.get(0));
        this.protocol = components.get(2);

        if (components.get(1).contains("?")) {
            components = List.of(components.get(1).split("\\?"));
            this.url = components.get(0);
            setQuery(components.get(1));
            return;
        }
        this.url = components.get(1);
    }

    private void setQuery(String queriesString) {
        List<String> queries = List.of(queriesString.split("&"));
        List<String> splitQuery;
        for (String query : queries) {
            splitQuery = List.of(query.split("="));
            this.query.put(splitQuery.get(0), splitQuery.get(1));
        }
    }

    private void setComponents(List<String> headerLines) {
        List<String> splitLine;
        for (String headerLine : headerLines) {
            splitLine = List.of(headerLine.split(": "));
            this.components.put(splitLine.get(0), splitLine.get(1));
        }
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public HttpMethod getMethod() {
        return method;
    }
}
