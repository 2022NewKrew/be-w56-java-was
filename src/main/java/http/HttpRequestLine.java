package http;

import java.util.Map;
import java.util.StringTokenizer;

import lombok.Getter;

import util.HttpRequestUtils;

@Getter
public class HttpRequestLine {
    private String method;
    private String url;
    private String version;
    private Map<String, String> queryParams;

    public HttpRequestLine(String requestLine) {
        StringTokenizer st = new StringTokenizer(requestLine);
        this.method = st.nextToken();
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
