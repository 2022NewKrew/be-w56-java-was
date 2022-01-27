package webserver.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestUri {
    private static final String URI_SEPARATOR = "\\?";
    private static final String QUERY_SEPARATOR = "&";
    private static final String PARAM_SEPARATOR = "=";
    private static final String PATH_SEPARATOR = "/";

    private final String url;
    private final Map<String, String> params;
    private final List<String> pathVariables;

    private RequestUri(String url) {
        this(url, new HashMap<>(), new ArrayList<>());
    }

    public RequestUri(String url, Map<String, String> params, List<String> pathVariables) {
        this.url = url;
        this.params = params;
        this.pathVariables = pathVariables;
    }

    public static RequestUri from(String uri) {
        List<String> urlAndQueries = List.of(uri.split(URI_SEPARATOR));
        String url = urlAndQueries.get(0);

        if (!isContainQueries(urlAndQueries)) return new RequestUri(url);

        Map<String, String> params = new HashMap<>();
        List<String> pathVariables = new ArrayList<>();

        for (String query : urlAndQueries.get(1).split(QUERY_SEPARATOR)) {
            List<String> keyAndValue = List.of(query.split(PARAM_SEPARATOR));
            if(isParam(keyAndValue)){
                String value = detachValue(keyAndValue.get(1), pathVariables);
                params.put(keyAndValue.get(0), value);
            }
        }
        return new RequestUri(url, params, pathVariables);

    }

    private static boolean isParam(List<String> keyAndValue) {
        return keyAndValue.size() == 2;
    }

    private static String detachValue(String value, List<String> pathVariables) {
        List<String> split = List.of(value.split(PATH_SEPARATOR));
        for (String pathVariable : split.subList(1, split.size())) {
            pathVariables.add(pathVariable);
        }
        return split.get(0);
    }

    private static boolean isContainQueries(List<String> urlAndQueries) {
        return urlAndQueries.size() > 1;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public List<String> getPathVariables() {
        return pathVariables;
    }

    @Override
    public String toString() {
        return "RequestUri{" +
                "url='" + url + '\'' +
                ", params=" + params +
                ", pathVariables=" + pathVariables +
                '}';
    }
}
