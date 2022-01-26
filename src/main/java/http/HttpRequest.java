package http;

import util.HttpRequestUtils;

import java.util.Map;

public class HttpRequest {

    private RequestLine requestLine;
    private Header header = new Header();

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Header getHeader() {
        return header;
    }

    public void setRequestLine(String line) {
        Map<String, String> requestLineMap = HttpRequestUtils.parseRequestLine(line);
        this.requestLine = new RequestLine(requestLineMap.get("method"), requestLineMap.get("url"), requestLineMap.get("protocol"));
    }

    public void setHeaderValue(String line) {
        HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
        // System.out.println(pair.toString());
        this.header.setValue(pair.getKey(), pair.getValue());
    }
}
