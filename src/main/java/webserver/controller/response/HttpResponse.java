package webserver.controller.response;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import webserver.common.Status;

public class HttpResponse {
    private String path;
    private Status status;
    private Map<String, String> header = Maps.newHashMap();

    public static HttpResponse of(String path, Status status) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.path = path;
        httpResponse.status = status;
        return httpResponse;
    }

    public String getPath() {
        return path;
    }

    public Status getStatus() {
        return status;
    }

    public void addHeaderAttribute(String key, String value) {
        System.out.println("add key = " + key+" header val = "+ value);
        header.put(key, value);
    }

    public String getHeaderString(){
        StringBuilder sb = new StringBuilder();
        for (Entry<String, String> e : header.entrySet()) {
            sb.append(String.format("%s: %s\r\n", e.getKey(), e.getValue()));
        }
        return sb.toString();
    }

}
