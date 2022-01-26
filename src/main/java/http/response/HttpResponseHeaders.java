package http.response;

import util.Pair;

import java.util.ArrayList;
import java.util.List;

public class HttpResponseHeaders {
    private List<Pair> headers;

    public HttpResponseHeaders(List<Pair> headers) {
        this.headers = headers;
    }

    public HttpResponseHeaders() {
        headers = new ArrayList<>();
    }

    public void addHeader(Pair header){
        headers.add(header);
    }

    public String getHeaderByKey(String key){
        return headers.stream().filter(pair -> pair.getKey().equals(key))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Response 헤더에 해당 key 가 없습니다"))
                .toString();
    }
}
