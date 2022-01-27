package web.http.request;

import util.Pair;

import java.util.List;

public class HttpRequestHeaders {
    private final List<Pair> headers;

    public HttpRequestHeaders(List<Pair> headers) {
        this.headers = headers;
    }

    public List<Pair> getHeaders() {
        return headers;
    }

    public boolean isHeader(String key){
        return headers.stream().anyMatch(pair -> pair.getKey().equals(key));
    }

    public String getHeaderValueByKey(String key){
        return headers.stream().filter(pair -> pair.getKey().equals(key))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Request 헤더에 해당 key 가 없습니다"))
                .getValue();
    }
}
