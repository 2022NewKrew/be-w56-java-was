package infrastructure.dto;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import http.common.Status;

public class AppResponse {
    private String path;
    private Status status;
    private Map<String, String> header = Maps.newHashMap();

    public static AppResponse of(String path, Status status) {
        AppResponse httpResponse = new AppResponse();
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
