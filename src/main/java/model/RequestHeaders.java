package model;

import util.HttpRequestUtils;

import java.util.ArrayList;
import java.util.List;

public class RequestHeaders {
    private final List<HttpRequestUtils.Pair> requestHeaders;

    private RequestHeaders(List<HttpRequestUtils.Pair> requestHeaders) {
        this.requestHeaders = new ArrayList<>(requestHeaders);
    }

    public static RequestHeaders from(List<HttpRequestUtils.Pair> requestHeaders) {
        return new RequestHeaders(requestHeaders);
    }
}
