package webserver;

import mapper.AssignedModelKey;

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
        this.url = firstLineSplit[1];
        this.protocol = firstLineSplit[2];

        this.header = header;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> makeModel(){
        Map<String, String> model = new HashMap<>(body);

        model.put(AssignedModelKey.REQUEST_URL, url);

        return model;
    }
}
