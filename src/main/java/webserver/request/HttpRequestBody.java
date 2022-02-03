package webserver.request;

import util.UrlQueryUtils;

import java.util.Map;

public class HttpRequestBody {
    private final Map<String, String> bodyMap;

    private HttpRequestBody(Map<String, String> bodyMap) {
        this.bodyMap = bodyMap;
    }

    public static HttpRequestBody makeHttpRequestBody(char[] bodyData){
        return new HttpRequestBody(UrlQueryUtils.parseUrlQuery(new String(bodyData)));
    }

    public static HttpRequestBody makeHttpRequestBody(String paramater){
        String[] split = paramater.split("\\?");

        paramater = split.length == 2 ? split[1] : "";
        return new HttpRequestBody(UrlQueryUtils.parseUrlQuery(paramater));
    }

    public Map<String, String> getBodyMap() {
        return bodyMap;
    }
}
