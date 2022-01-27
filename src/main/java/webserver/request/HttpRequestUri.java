package webserver.request;

import org.apache.commons.lang3.StringUtils;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestUri {
    private String url;
    private Map<String, String> queries;

    public HttpRequestUri(String url) {
        this(url, new HashMap<>());
    }

    public HttpRequestUri(String url, Map<String, String> queries) {
        this.url = url;
        this.queries = queries;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getQueries() {
        return queries;
    }

    public boolean isForStaticContent() {
        return url.contains(".");
    }

    public static class Builder {
        public static HttpRequestUri createFromStringUri(String uri) {
            String[] urlAndQueries = StringUtils.split(uri, "?");
            String url = urlAndQueries[0];
            if(isContainQueryString(urlAndQueries)) {
                Map<String, String> queries = HttpRequestUtils.parseQueryString(urlAndQueries[1]);
                return new HttpRequestUri(url, queries);
            }
            return new HttpRequestUri(url);
        }

        private static boolean isContainQueryString(String[] urlAndQueries) {
            return urlAndQueries.length > 1;
        }
    }
}
