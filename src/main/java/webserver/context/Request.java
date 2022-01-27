package webserver.context;

import java.util.Map;

public class Request {

    private final String method;
    private final String url;
    private final Map<String,String> paramMap;
    private final Map<String,String> headerDataMap;
    private final Map<String,String> bodyDataMap;

    public Request(String method, String url, Map<String, String> paramMap, Map<String, String> headerDataMap, Map<String, String> bodyDataMap) {
        this.method = method;
        this.url = url;
        this.paramMap = paramMap;
        this.headerDataMap = headerDataMap;
        this.bodyDataMap = bodyDataMap;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public Map<String, String> getHeaderDataMap() {
        return headerDataMap;
    }

    public Map<String, String> getBodyDataMap() {
        return bodyDataMap;
    }
}
