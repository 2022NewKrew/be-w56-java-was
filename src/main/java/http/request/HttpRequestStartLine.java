package http.request;

import http.util.HttpRequestUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class HttpRequestStartLine {
    private static final String ROOT_URI = "/";
    private static final String ROOT_INDEX = "/index.html";

    private final String method;
    private final String targetUri;
    private final String httpVersion;
    private final Map<String, String> queryParams;

    public HttpRequestStartLine(String line) {
        if(Objects.isNull(line)){
            throw new IllegalArgumentException();
        }
        String[] tokens = parseStartLine(line);

        this.method = tokens[0];
        this.httpVersion = tokens[2];

        if(tokens[1].contains("?")){
            String[] split = parseTargetUri(tokens[1]);
            this.targetUri = split[0];
            this.queryParams = HttpRequestUtils.parseQueryString(split[1]);
            return;
        }

        this.targetUri = tokens[1];
        this.queryParams = null;
    }

    private String[] parseStartLine(String line){
        return  HttpRequestUtils.parseHttpRequestStartLine(line);
    }

    private String[] parseTargetUri(String uri){
        return uri.split("\\?");
    }

    public String getMethod() {
        return method;
    }

    public String getTargetUri() {
        return targetUri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getQueryParams() {
        return Collections.unmodifiableMap(queryParams);
    }
}
