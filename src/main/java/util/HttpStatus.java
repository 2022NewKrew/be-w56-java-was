package util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-26 026
 * Time: 오후 6:41
 */
public enum HttpStatus {
    OK("OK", "200"),
    REDIRECT("REDIRECT", "302"),
    NOT_FOUND("NOT FOUND", "404");

    private String statusName;
    private String statusCode;
    private static Map<String, HttpStatus> httpMethodMap;

    static {
        httpMethodMap = initializeHttpMethodMap();
    }

    HttpStatus(String statusName, String statusCode) {
        this.statusName = statusName;
        this.statusCode = statusCode;
    }

    private static Map<String, HttpStatus> initializeHttpMethodMap() {
        Map<String, HttpStatus> httpMethodMap = new HashMap<>();
        HttpStatus[] httpStatuses = values();
        for (HttpStatus httpStatus : httpStatuses) {
            httpMethodMap.put(httpStatus.statusName, httpStatus);
        }
        return httpMethodMap;
    }

    public HttpStatus of(String statusName) {
        return httpMethodMap.get(statusName);
    }

    public String getStatusName() {
        return statusName;
    }
}
