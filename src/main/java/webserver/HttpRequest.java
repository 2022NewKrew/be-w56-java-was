package webserver;

import mapper.AssignedModelKey;
import util.UrlQueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String url;
    private final String protocol;
    private Map<String, String> header;
    private Map<String, String> body;

    public HttpRequest(String[] firstLineSplit, Map<String, String> header, Map<String, String> body){
        this.method = firstLineSplit[0];
        String priUrl = firstLineSplit[1];
        this.protocol = firstLineSplit[2];
        this.url = priUrl.split("\\?")[0];

        this.header = header;

        if(priUrl.split("\\?").length == 2 && "GET".equals(method))
            this.body = UrlQueryUtils.parseUrlQuery(priUrl);
        else
            this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getBody() {
        return body;
    }
}
