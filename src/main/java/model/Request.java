package model;

import util.HttpRequestUtils.Pair;

import java.util.List;

public class Request {
    private final String method;
    private final String path;
    private final List<Pair> header;

    public Request(String method, String path, List<Pair> header) {
        this.method = method;
        this.path = path;
        this.header = header;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public List<Pair> getHeader() {
        return header;
    }
}
