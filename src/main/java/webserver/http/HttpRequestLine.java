package webserver.http;

import java.util.Map;
import java.util.StringTokenizer;

import lombok.Getter;

import webserver.util.HttpRequestUtils;

@Getter
public class HttpRequestLine {
    private HttpRequestMethod method;
    private String url;
    private String version;
    private Map<String, String> queryParams;

    public HttpRequestLine(String requestLine) {
        StringTokenizer st = new StringTokenizer(requestLine);
        this.method = HttpRequestMethod.valueOf(st.nextToken());
        setUrlAndParams(st.nextToken());
        this.version = st.nextToken();
    }

    private void setUrlAndParams(String url) {
        StringTokenizer st = new StringTokenizer(url, "?");
        this.url = st.nextToken();

        if (st.hasMoreTokens()) {
            this.queryParams = HttpRequestUtils.parseQueryString(st.nextToken());
        }
    }
}
