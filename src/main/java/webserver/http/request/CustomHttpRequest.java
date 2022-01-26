package webserver.http.request;

import java.util.Map;

import static util.HttpRequestUtils.*;

public class CustomHttpRequest {
    private CustomRequestHeader customHeader;

    public CustomHttpRequest() {
        customHeader = new CustomRequestHeader();
    }

    public Map<String, String> getRequestParam() {
        return customHeader.getRequestParam();
    }

    public void putHeaderData(Pair data) {
        customHeader.addHeaderData(data);
    }

    public void putHeaderRequestLine(Map<String, String> requestLineData) {
        customHeader.setRequestLineData(requestLineData);
    }

    public void putRequestParam(Map<String, String> param) {
        customHeader.setRequestParam(param);
    }

    public Map<String, String> getRequestLineMap() {
        return customHeader.getRequestLineMap();
    }

    public String getRequestURI() {
        return customHeader.getRequestUrl();
    }

    public String getHeaderAttr(String key) {
        return customHeader.getRequestHeaderAttr(key);
    }

}
