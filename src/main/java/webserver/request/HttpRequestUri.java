package webserver.request;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestUri {
    private String url;
    private Map<String, String[]> queries;

    public HttpRequestUri(String uri) {
        String[] urlAndQueries = StringUtils.split(uri, "?");
        this.url = urlAndQueries[0];

        this.queries = new HashMap<>();
        if (isContainQueries(urlAndQueries)) {
            String[] queriesBeforeFormat = urlAndQueries[1].split("&");
            setQueries(queriesBeforeFormat);
        }
    }

    private boolean isContainQueries(String[] urlAndQueries) {
        return urlAndQueries.length > 1;
    }

    private void setQueries(String[] queriesBeforeFormat) {
        for (String query : queriesBeforeFormat) {
            String[] keyAndValues = StringUtils.split(query, "=");
            String key = keyAndValues[0];
            String[] values = StringUtils.split(keyAndValues[1], ",");
            this.queries.put(key, values);
        }
    }

    public String getUrl() {
        return url;
    }

    public boolean isForStaticContent() {
        return url.contains(".");
    }
}
