package webserver.response;

import java.util.Map;

import com.google.common.collect.Maps;

public class HttpResponse {
    private String path;
    private Status status;
    private Map<String, String> model = Maps.newHashMap();

    public static HttpResponse of(String path, Status status, Map<String, String> model) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.path = path;
        httpResponse.status = status;
        httpResponse.model = model;
        return httpResponse;
    }

    public String getPath() {
        return path;
    }

    public Status getStatus() {
        return status;
    }

    public Map<String, String> getModel() {
        return model;
    }
}
