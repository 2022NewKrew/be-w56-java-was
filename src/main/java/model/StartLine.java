package model;

import com.google.common.base.Strings;
import exceptions.BadRequestFormatException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StartLine {

    private static Map<String, String> parseQuery(String target) {
        if (!target.contains("?")) {
            return new HashMap<>();
        }

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
        if (Strings.isNullOrEmpty(startLine)) {
            throw new BadRequestFormatException("start line이 존재하지 않습니다");
        }
        String[] token = startLine.split(" ");

        return new StartLine(HttpMethod.fromString(token[0]), parseUrl(token[1]), parseQuery(token[1]), token[2]);
    }

    private final HttpMethod httpMethod;
    private final String url;
    private final Map<String, String> query;
    private final String httpVersion;

    private StartLine(HttpMethod httpMethod, String url, Map<String, String> query, String httpVersion) {
        if (Objects.isNull(httpMethod) || Objects.isNull(url) || Objects.isNull(query) || Objects.isNull(httpVersion)) {
            throw new BadRequestFormatException(
                    String.format("start line의 인자가 부족합니다 %s %s %s %s", httpMethod, url, query, httpVersion));
        }
        this.httpMethod = httpMethod;
        this.url = url;
        this.query = Collections.unmodifiableMap(query);
        this.httpVersion = httpVersion;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getQuery() {
        return query;
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
