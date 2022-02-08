package model;

import java.util.Map;

public class RequestLine {
    private static final String QUERY_STRING_REGEX = "\\?";

    private final String method;
    private final String url;
    private final QueryStrings queryStrings;
    private final String version;

    private RequestLine(String method, String url, String version, QueryStrings queryStrings) {
        this.method = method;
        this.url = url;
        this.version = version;
        this.queryStrings = queryStrings;
    }

    public static RequestLine from(String[] requestLineTokens) {
        String[] urlParameters = requestLineTokens[1].split(QUERY_STRING_REGEX);

        QueryStrings queryStrings = (urlParameters.length > 1) ?
                QueryStrings.from(urlParameters[1]) : QueryStrings.from(null);

        return new RequestLine(requestLineTokens[0], urlParameters[0], requestLineTokens[2], queryStrings);
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public boolean isFile() {
        return url.endsWith(".html") ||
                url.endsWith(".css") ||
                url.endsWith(".js") ||
                url.endsWith(".ttf") ||
                url.endsWith(".woff") ||
                url.endsWith(".ico");
    }

    public Map<String, String> getQueryStrings() {
        return queryStrings.getQueryStrings();
    }

    public boolean isEqualUrl(String url) {
        return this.url.equals(url);
    }

    @Override
    public String toString() {
        return "RequestLine{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", queryStrings=" + queryStrings +
                ", version='" + version + '\'' +
                '}';
    }
}
