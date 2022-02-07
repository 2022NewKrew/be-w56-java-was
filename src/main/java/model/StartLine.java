package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartLine {

    private static Map<String, String> parseQuery(String target) {
        if (!target.contains("?"))
            return new HashMap<>();

        Map<String, String> queryMap = new HashMap<>();
        String parseString = List.of(target.split("\\?")).get(1);

        List<String> queries = List.of(parseString.split("&"));
        for (String query : queries) {
            List<String> splitQuery = List.of(query.split("="));
            queryMap.put(splitQuery.get(0), splitQuery.get(1));
        }
        return queryMap;
    }

    private static String parseUrl(String target) {
        return List.of(target.split("\\?")).get(0);
    }

    public static StartLine of(String startLine) {
        String[] token = startLine.split(" ");

        return new StartLine(HttpMethod.fromString(token[0]), parseUrl(token[1]), parseQuery(token[1]), token[2]);
    }

    private final HttpMethod httpMethod;
    private final String url;
    private final Map<String, String> query;
    private final String httpVersion;

    private StartLine(HttpMethod httpMethod, String url, Map<String, String> query, String httpVersion) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.query = query;
        this.httpVersion = httpVersion;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "StartLine{" +
                "httpMethod=" + httpMethod +
                ", url='" + url + '\'' +
                ", query=" + query +
                ", httpVersion='" + httpVersion + '\'' +
                '}';
    }
}
