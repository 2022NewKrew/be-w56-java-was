package webserver.http.request;

import com.google.common.collect.Maps;
import webserver.http.request.body.RequestBody;
import webserver.http.request.header.RequestHeader;

import java.util.Map;

public class InfoMap {
    private final Map<String, String> infoMap;

    public InfoMap(RequestHeader requestHeader, RequestBody requestBody) {
        String httpMethod = requestHeader.getHttpMethod();
        if (httpMethod.equals("GET")) {
            infoMap = requestHeader.getParameterMap().getInstance();
        } else if (httpMethod.equals("POST")) {
            infoMap = requestBody.getBodyMap().getInstance();
        } else {
            infoMap = Maps.newHashMap();
        }
    }

    public String get(String key) {
        return infoMap.get(key);
    }

}
