package webserver.domain;

import lombok.Builder;
import util.HttpRequestUtils;

public class StartLine {

    private final String httpMethod;
    private final String path;
    private final QueryString queryString;
    private final String httpVersion;

    @Builder
    private StartLine(String httpMethod, String path, QueryString queryString, String httpVersion) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.queryString = new QueryString(queryString);
        this.httpVersion = httpVersion;
    }

    @Builder
    public StartLine(StartLine startLine) {
        this(startLine.httpMethod, startLine.path, startLine.queryString, startLine.httpVersion);
    }

    public static StartLine createStartLine(String line) {
        String[] splitedLine = HttpRequestUtils.parseStartLine(line);
        String[] splitedPath = HttpRequestUtils.parsePath(splitedLine[1]);
        QueryString queryString = splitedPath.length > 1 ?
            QueryString.createQueryString(splitedPath[1]) : QueryString.createQueryString(null);
        return StartLine.builder()
            .httpMethod(splitedLine[0])
            .path(splitedPath[0])
            .queryString(queryString)
            .httpVersion(splitedLine[2])
            .build();
    }

    public String getPath() {
        return this.path;
    }

    public String getQueryStringAttribute(String key) {
        return queryString.getQueryStringAttribute(key);
    }

    public boolean isFile() {
        int idx = this.path.lastIndexOf("/");
        String endString = this.path.substring(idx + 1);
        return endString.contains(".");
    }

    public boolean startsWith(String path) {
        return this.path.startsWith(path);
    }

    public String getHttpMethod() {
        return this.httpMethod;
    }
}
