package httpmodel;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import util.HttpRequestUtils;

public class Uri {

    private final String resourceUri;
    private final Map<String, String> queryString;

    public Uri(String resourceUri, Map<String, String> queryString) {
        this.resourceUri = resourceUri;
        this.queryString = queryString;
    }

    public static Uri valueOf(@Nonnull String fullUri) {
        int index = fullUri.indexOf("?");
        if (index == -1) {
            return new Uri(fullUri, new HashMap<>());
        }
        String uri = fullUri.substring(0, index);
        Map<String, String> queryString = HttpRequestUtils.parseQueryString(
            fullUri.substring(index + 1));
        return new Uri(uri, queryString);
    }

    public String getQuery(String key) {
        return queryString.get(key);
    }

    public boolean isUriFile() {
        return resourceUri.endsWith(".html")
            || resourceUri.endsWith(".css")
            || resourceUri.endsWith(".js")
            || resourceUri.endsWith(".ttf")
            || resourceUri.endsWith(".ico")
            || resourceUri.endsWith(".woff");
    }

    public String getResourceUri() {
        return resourceUri;
    }
}
